package com.ruimin.ifinflow.model.flowmodel.cache;

import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TemplateCacheManager {
    private static HashMap<String, Object> templateCacheMap = new HashMap();

    private static TemplateCacheManager instance = null;


    public static TemplateCacheManager getInstance() {
        if (instance == null) {
            synchronized (TemplateCacheManager.class) {
                if (instance == null) {
                    instance = new TemplateCacheManager();
                }
            }
        }
        return instance;
    }


    private synchronized TemplateCache getTemplateCache(String key) {
        return (TemplateCache) templateCacheMap.get(key);
    }


    private synchronized boolean hasTemplateCache(String key) {
        return templateCacheMap.containsKey(key);
    }


    public synchronized void clearAll() {
        templateCacheMap.clear();
    }


    public synchronized void clearOnly(String key) {
        templateCacheMap.remove(key);
    }


    public synchronized void putTemplateCache(String key, TemplateCache obj) {
        templateCacheMap.put(key, obj);
    }


    public TemplateCache getTemplateCacheInfo(String key) {
        if (hasTemplateCache(key)) {
            return getTemplateCache(key);
        }
        TemplateVo templateVo = null;

        TemplateCache tc = new TemplateCache();


        if (key.indexOf("_") > 0) {
            String packageId = key.substring(0, key.lastIndexOf("_"));
            String templateId = key.substring(key.lastIndexOf("_") + 1, key.lastIndexOf("-"));
            int version = Integer.parseInt(key.substring(key.lastIndexOf("-") + 1));
            templateVo = tc.getTemplateVo(templateId, packageId, version);
        } else {
            templateVo = tc.getTemplateVo(key);
        }
        tc.setDeploymentId(key);
        tc.setTemplateVo(templateVo);
        putTemplateCache(key, tc);
        return tc;
    }


    public int getTemplateCacheSize() {
        return templateCacheMap.size();
    }

    public ArrayList<String> getTemplateCacheAllKey() {
        ArrayList<String> a = new ArrayList();
        try {
            Iterator<Map.Entry<String, Object>> i = templateCacheMap.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) i.next();
                a.add((String) entry.getKey());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return a;
    }
}