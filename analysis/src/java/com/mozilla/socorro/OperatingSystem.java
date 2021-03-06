/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.mozilla.socorro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mozilla.util.MapValueComparator;

public class OperatingSystem {

	private String name = null;
	private int count = 0;
	private Map<String, Signature> signatures = new HashMap<String, Signature>();
	private Map<String, Integer> coreCounts = new HashMap<String, Integer>();
	private Map<String, Module> moduleCounts = new HashMap<String, Module>();
	private Map<String, Module> addonCounts = new HashMap<String, Module>();

	public OperatingSystem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<String, Signature> getSignatures() {
		return signatures;
	}

	public void addSignature(String name, Signature signature) {
		this.signatures.put(name, signature);
	}

	public void setSignatures(Map<String, Signature> signatures) {
		this.signatures = signatures;
	}

	public Map<String, Integer> getCoreCounts() {
		return coreCounts;
	}

	public List<Map.Entry<String, Integer>> getSortedCoreCounts() {
		List<Map.Entry<String, Integer>> coreCountPairs = new ArrayList<Map.Entry<String,Integer>>(coreCounts.entrySet());
		Collections.sort(coreCountPairs, Collections.reverseOrder(new MapValueComparator()));
		return coreCountPairs;
	}

	public void incrementCoreCount(String arch, int count) {
		int existingCount = 0;
		if (coreCounts.containsKey(arch)) {
			existingCount = coreCounts.get(arch);
		}
		coreCounts.put(arch, existingCount + count);
	}

	public void setCoreCounts(Map<String, Integer> coreCounts) {
		this.coreCounts = coreCounts;
	}

	public Map<String, Module> getModuleCounts() {
		return moduleCounts;
	}

	public List<Module> getSortedModuleCounts() {
		List<Module> modules = new ArrayList<Module>(moduleCounts.values());
		Collections.sort(modules, Collections.reverseOrder(new Module.ModuleComparator()));
		return modules;
	}

	public void incrementModuleCount(String moduleName, String moduleVersion, int count) {
		Module module = null;
		if (moduleCounts.containsKey(moduleName)) {
			module = moduleCounts.get(moduleName);
		} else {
			module = new Module(moduleName);
		}
		module.setCount(module.getCount() + count);
		module.incrementVersionCount(moduleVersion, count);
		moduleCounts.put(moduleName, module);
	}

	public void setModuleCounts(Map<String, Module> moduleCounts) {
		this.moduleCounts = moduleCounts;
	}

	public Map<String, Module> getAddonCounts() {
		return addonCounts;
	}

	public List<Module> getSortedAddonCounts() {
		List<Module> addons = new ArrayList<Module>(addonCounts.values());
		Collections.sort(addons, Collections.reverseOrder(new Module.ModuleComparator()));
		return addons;
	}

	public void incrementAddonCount(String addonName, String addonVersion, int count) {
		Module module = null;
		if (addonCounts.containsKey(addonName)) {
			module = addonCounts.get(addonName);
		} else {
			module = new Module(addonName);
		}
		module.setCount(module.getCount() + count);
		module.incrementVersionCount(addonVersion, count);
		addonCounts.put(addonName, module);
	}

	public void setAddonCounts(Map<String, Module> addonCounts) {
		this.addonCounts = addonCounts;
	}

}
