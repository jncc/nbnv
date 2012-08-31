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
import nbn.common.mapping.arcgis.MappingRequestDAO;
import nbn.common.mapping.arcgis.SingleSpeciesRequest;

/**
 *
 * @author Administrator
 */
public class SingleSpeciesRequestCache {
    private List<SSREntry> _list;
    //private Timer _timer;
    private int _ttl;

    public SingleSpeciesRequestCache() {
        _list = new ArrayList<SSREntry>();

        //_timer = new Timer();
        //_timer.schedule(new Watchdog(_list), 0, 1800 * 1000);

        _ttl = Integer.valueOf(ResourceBundle.getBundle("nbnServerSpecificProperties").getString("nbn.nim.cache.ttl"));
    }

    public String getRequestKey(SingleSpeciesRequest request) {
        SSREntry re = getEntry(request);

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

    public synchronized void addRequest(SingleSpeciesRequest request) {
        SSREntry entry = new SSREntry();
        entry.request = request;
        entry.created = Calendar.getInstance().getTime();
        entry.accessed = Calendar.getInstance().getTime();
        entry.built = false;
        _list.add(entry);
    }

    public void completeRequest(SingleSpeciesRequest request) {
        SSREntry re = getEntry(request);
        re.built = true;
    }

    private synchronized SSREntry getEntry(SingleSpeciesRequest request) {
        for (SSREntry re : _list) {
            SingleSpeciesRequest r = re.request;
            if (r.getUser().getUserKey() == request.getUser().getUserKey()
                    && r.getTaxonKey().equals(request.getTaxonKey())
                    && r.getStartYear() == request.getStartYear()
                    && r.getEndYear() == request.getEndYear()
                    && r.getDatasets().equals(request.getDatasets())
                    && r.getPrivilegesList().equals(request.getPrivilegesList())) {
                re.accessed = Calendar.getInstance().getTime();
                return re;
            }
        }

        return null;
    }

    class SSREntry {
        public SingleSpeciesRequest request;
        public Date created;
        public Date accessed;
        public boolean built;
    }

    class Watchdog extends TimerTask {
        private List<SSREntry> _list;

        Watchdog(List<SSREntry> list) {
            _list = list;
        }

        @Override
        public void run() {
            List<SSREntry> forDelete = new ArrayList<SSREntry>();

            for (SSREntry re : _list) {
                if (Calendar.getInstance().getTimeInMillis() - re.created.getTime() > _ttl) {
                    forDelete.add(re);
                }
            }

            MappingRequestDAO rdao = null;

            try {
                rdao = new MappingRequestDAO();

                for (SSREntry re : forDelete) {
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
