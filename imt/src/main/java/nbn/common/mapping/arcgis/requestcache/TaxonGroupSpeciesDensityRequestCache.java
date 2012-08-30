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
import nbn.common.mapping.arcgis.TaxonGroupSpeciesDensityRequest;

/**
 *
 * @author Administrator
 */
public class TaxonGroupSpeciesDensityRequestCache {
    private List<TGSDREntry> _list;
    //private Timer _timer;
    private int _ttl;

    public TaxonGroupSpeciesDensityRequestCache() {
        _list = new ArrayList<TGSDREntry>();

        //_timer = new Timer();
        //_timer.schedule(new Watchdog(_list), 0, 1800 * 1000);

        _ttl = Integer.valueOf(ResourceBundle.getBundle("nbnServerSpecificProperties").getString("nbn.nim.cache.ttl"));
    }

    public String getRequestKey(TaxonGroupSpeciesDensityRequest request) {
        TGSDREntry re = getEntry(request);

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

    public synchronized void addRequest(TaxonGroupSpeciesDensityRequest request) {
        TGSDREntry entry = new TGSDREntry();
        entry.request = request;
        entry.created = Calendar.getInstance().getTime();
        entry.accessed = Calendar.getInstance().getTime();
        entry.built = false;
        _list.add(entry);
    }

    public void completeRequest(TaxonGroupSpeciesDensityRequest request) {
        TGSDREntry re = getEntry(request);
        re.built = true;
    }

    private synchronized TGSDREntry getEntry(TaxonGroupSpeciesDensityRequest request) {
        for (TGSDREntry re : _list) {
            TaxonGroupSpeciesDensityRequest r = re.request;
            if (r.getUser().getUserKey() == request.getUser().getUserKey()
                    && r.getTaxonGroupKey().equals(request.getTaxonGroupKey())
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

    class TGSDREntry {
        public TaxonGroupSpeciesDensityRequest request;
        public Date created;
        public Date accessed;
        public boolean built;
    }

    class Watchdog extends TimerTask {
        private List<TGSDREntry> _list;

        Watchdog(List<TGSDREntry> list) {
            _list = list;
        }

        @Override
        public void run() {
            List<TGSDREntry> forDelete = new ArrayList<TGSDREntry>();

            for (TGSDREntry re : _list) {
                if (Calendar.getInstance().getTimeInMillis() - re.created.getTime() > _ttl) {
                    forDelete.add(re);
                }
            }

            MappingRequestDAO rdao = null;

            try {
                rdao = new MappingRequestDAO();

                for (TGSDREntry re : forDelete) {
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
