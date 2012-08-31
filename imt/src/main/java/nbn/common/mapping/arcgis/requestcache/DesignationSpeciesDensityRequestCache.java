/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.arcgis.requestcache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbn.common.mapping.arcgis.DesignationSpeciesDensityRequest;
import nbn.common.mapping.arcgis.MappingRequestDAO;

/**
 *
 * @author Administrator
 */
public class DesignationSpeciesDensityRequestCache {
    private List<DSDREntry> _list;
    //private Timer _timer;
    private int _ttl;

    public DesignationSpeciesDensityRequestCache() {
        _list = new ArrayList<DSDREntry>();

        //_timer = new Timer();
        //_timer.schedule(new Watchdog(_list), 0, 1800 * 1000);

        _ttl = Integer.valueOf(ResourceBundle.getBundle("nbnServerSpecificProperties").getString("nbn.nim.cache.ttl"));
    }

    public String getRequestKey(DesignationSpeciesDensityRequest request) {
        DSDREntry re = getEntry(request);

        if (re != null) {
            while (re.built == false) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DesignationSpeciesDensityRequestCache.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return re.request.getRequestKey();
        } else {
            return null;
        }
    }

    public synchronized void addRequest(DesignationSpeciesDensityRequest request) {
        DSDREntry entry = new DSDREntry();
        entry.request = request;
        entry.created = Calendar.getInstance().getTime();
        entry.accessed = Calendar.getInstance().getTime();
        entry.built = false;
        _list.add(entry);
    }

    public void completeRequest(DesignationSpeciesDensityRequest request) {
        DSDREntry re = getEntry(request);
        re.built = true;
    }

    private synchronized DSDREntry getEntry(DesignationSpeciesDensityRequest request) {
        for (DSDREntry re : _list) {
            DesignationSpeciesDensityRequest r = re.request;
            if (r.getUser().getUserKey() == request.getUser().getUserKey()
                    && r.getDesignationKey().equals(request.getDesignationKey())
                    && r.getStartYear() == request.getStartYear()
                    && r.getEndYear() == request.getEndYear()
                    && r.getDatasets().equals(request.getDatasets())
                    && r.getPrivilegesList().equals(request.getPrivilegesList())){
                re.accessed = Calendar.getInstance().getTime();
                return re;
            } 
        }
        return null;
    }

    class DSDREntry {
        public DesignationSpeciesDensityRequest request;
        public Date created;
        public Date accessed;
        public boolean built;
    }

    class Watchdog extends TimerTask {
        private List<DSDREntry> _list;

        Watchdog(List<DSDREntry> list) {
            _list = list;
        }

        @Override
        public void run() {
            List<DSDREntry> forDelete = new ArrayList<DSDREntry>();

            for (DSDREntry re : _list) {
                if (Calendar.getInstance().getTimeInMillis() - re.created.getTime() > _ttl) {
                    forDelete.add(re);
                }
            }

            MappingRequestDAO rdao = null;

            try {
                rdao = new MappingRequestDAO();

                for (DSDREntry re : forDelete) {
                    rdao.disposeRequest(re.request);
                    _list.remove(re);
                }
            } catch (Exception ex) {
            } finally {
                if (rdao != null)
                    rdao.dispose();
            }
        }
    }
}
