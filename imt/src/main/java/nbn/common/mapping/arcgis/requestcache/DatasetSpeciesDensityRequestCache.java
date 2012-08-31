/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbn.common.mapping.arcgis.requestcache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import nbn.common.mapping.arcgis.DatasetSpeciesDensityRequest;
import nbn.common.mapping.arcgis.MappingRequestDAO;

/**
 *
 * @author Administrator
 */
public class DatasetSpeciesDensityRequestCache {

    private List<DSDREntry> _list;
    //private Timer _timer;
    private int _ttl;

    public DatasetSpeciesDensityRequestCache() {
        _list = new ArrayList<DSDREntry>();

        //_timer = new Timer();
        //_timer.schedule(new Watchdog(_list), 0, 1800 * 1000);

        _ttl = Integer.valueOf(ResourceBundle.getBundle("nbnServerSpecificProperties").getString("nbn.nim.cache.ttl"));
    }

    public String getRequestKey(DatasetSpeciesDensityRequest request) {
        DSDREntry re = getEntry(request);

        if (re != null) {
            while (re.built == false) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                }
            }
            return re.request.getRequestKey();
        } else {
            return null;
        }
    }

    public synchronized void addRequest(DatasetSpeciesDensityRequest request) {
        DSDREntry entry = new DSDREntry();
        entry.request = request;
        entry.created = Calendar.getInstance().getTime();
        entry.accessed = Calendar.getInstance().getTime();
        entry.built = false;
        _list.add(entry);
    }

    public void completeRequest(DatasetSpeciesDensityRequest request) {
        DSDREntry re = getEntry(request);
        re.built = true;
    }

    private synchronized DSDREntry getEntry(DatasetSpeciesDensityRequest request) {
        for (DSDREntry re : _list) {
            DatasetSpeciesDensityRequest r = re.request;

            if (r.getUser().getUserKey() == request.getUser().getUserKey()
                    && r.getDatasetKey().equals(request.getDatasetKey())
                    && r.getStartYear() == request.getStartYear()
                    && r.getEndYear() == request.getEndYear()
                    && r.getPrivileges().hasViewSensitive() == request.getPrivileges().hasViewSensitive()
                    && r.getPrivileges().getBlurLevel() == request.getPrivileges().getBlurLevel()) {
                re.accessed = Calendar.getInstance().getTime();
                return re;
            }
        }

        return null;
    }

    class DSDREntry {
        public DatasetSpeciesDensityRequest request;
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
                if (rdao != null) {
                    rdao.dispose();
                }
            }
        }
    }
}
