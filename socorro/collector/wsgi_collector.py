import web
import time

from socorro.lib.ooid import createNewOoid
from socorro.lib.util import DotDict
from socorro.collector.throttler import DISCARD
from socorro.lib.datetimeutil import utc_now

#==============================================================================
class Collector(object):
    #--------------------------------------------------------------------------
    def __init__(self, config):
        self.config = config
        self.logger = self.config.logger
        self.throttler = config.throttler
        self.dump_id_prefix = config.collector.dump_id_prefix
        self.crash_storage = config.crash_storage
        self.dump_field = config.collector.dump_field

    #--------------------------------------------------------------------------
    uri = '/submit'
    #--------------------------------------------------------------------------

    #--------------------------------------------------------------------------
    def make_raw_crash (self, form):
        names = (name for name in form.keys() if name != self.dump_field)
        raw_crash = DotDict()
        for name in names:
            if isinstance(form[name], basestring):
                raw_crash[name] = form[name]
            else:
                raw_crash[name] = form[name].value
        raw_crash.timestamp = time.time()
        return raw_crash

    #--------------------------------------------------------------------------
    def POST(self, *args):
        the_form = web.input()
        raw_crash = self.make_raw_crash(the_form)
        dump = the_form[self.dump_field]

        current_timestamp = utc_now()
        raw_crash.submitted_timestamp = current_timestamp.isoformat()

        crash_id = createNewOoid(current_timestamp)
        self.logger.info('%s received', crash_id)

        raw_crash.legacy_processing = self.throttler.throttle(raw_crash)
        if raw_crash.legacy_processing == DISCARD:
            return "Discarded=1\n"

        result = self.config.crash_storage.save_raw_crash(
          raw_crash,
          dump,
          crash_id
        )
        return "CrashID=%s%s\n" % (self.dump_id_prefix, crash_id)