.. This Source Code Form is subject to the terms of the Mozilla Public
.. License, v. 2.0. If a copy of the MPL was not distributed with this
.. file, You can obtain one at http://mozilla.org/MPL/2.0/.

2.5.0 Database Updates
======================

This batch makes the following database changes:

bug #729195
	Modify Postgres database to correctly handle ESR releases.
	Note: no backfill will be performed.
	
bug #731000
	Add database table for rule-based data transformation

The above changes should take around 1/2 hour to deploy.

This upgrade should involve a halt to processing while 2.5 changes are deployed in order to ensure correct crash format.