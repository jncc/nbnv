window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.verification = function(json) {
    var _me = this;
    
    _me.verified = false;
    _me.incorrect = false;
    _me.uncertain = false;
    _me.unverified = false;
    
    if (typeof(json.verification) === 'undefined') { 
        _me.verified = true;
        _me.uncertain = true;
        _me.unverified = true;
    } else {
        processJSON(_me, json.verification);
    }
    
    function processJSON(_me, verification) { 
        resetSelected(_me);
        
        $.each(verification.verification, function(index, value) {
           if (value === "VERIFIED")  {
               _me.verified = true;
           } else if (value === "INCORRECT") {
               _me.incorrect = true;
           } else if (value === "UNCERTAIN") {
               _me.uncertain = true;
           } else if (value === "UNVERIFIED") {
               _me.unverified = true;
           }
        });
    };
        
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'verification')
            .append($('<span>').addClass('filterheader').append('Record Verification Status'))
            .append($('<span>').attr('id', 'verificationResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        var c_Verified = _me.verified ? "checked=\"checked\"" : "";
        var c_Incorrect = _me.incorrect ? "checked=\"checked\"" : ""; 
        var c_Uncertain = _me.uncertain ? "checked=\"checked\"" : "";
        var c_Unverified = _me.unverified ? "checked=\"checked\"" : "";
        
        var dataDiv = $('<div>')
            .append($('<span class="verificationInput"><input class="verificationCheckbox" type="checkbox" id="VERIFIED" ' + c_Verified + '/><label for="VERIFIED">Verified</label></span><span class="verificationDesc">Record has been accepted as true by a verifier or acceptable system.</span>'))
            .append($('<br class="verificationSplit" />'))
            .append($('<span class="verificationInput"><input class="verificationCheckbox" type="checkbox" id="INCORRECT" ' + c_Incorrect + '/><label for="INCORRECT">Incorrect</label></span><span class="verificationDesc">Record has been accepted as false by a verifier or acceptable system.</span>'))
            .append($('<br class="verificationSplit" />'))
            .append($('<span class="verificationInput"><input class="verificationCheckbox" type="checkbox" id="UNCERTAIN" ' + c_Uncertain + '/><label for="UNCERTAIN">Uncertain</label></span><span class="verificationDesc">Record has some uncertainty as to its validity.</span>'))
            .append($('<br class="verificationSplit" />'))
            .append($('<span class="verificationInput"><input class="verificationCheckbox" type="checkbox" id="UNVERIFIED" ' + c_Unverified + '/><label for="UNVERIFIED">Unverified</label></span><span class="verificationDesc">Record has no commentary or validity reports.</span>'));
                       
        return dataDiv;
    };
    
    this._postRender = function() {       
        $('.verificationCheckbox').change(function() {
            if ($(this).is(':checked')) {
                _me.setVerification($(this).attr('id'), true);
            } else {
                _me.setVerification($(this).attr('id'), false);
            }
        });  
        
        this._onExit();
    };
    
    this.setVerification = function(id, value) {
        var _me = this;
        
        if (id === "VERIFIED") {
            _me.verified = value;
        } else if (id === "INCORRECT") {
            _me.incorrect = value;
        } else if (id === "UNCERTAIN") {
            _me.uncertain = value;
        } else if (id === "UNVERIFIED") {
            _me.unverified = value;
        }
    };
    
    this._onEnter = function() {
        $("#verificationResult").text("");
    };
    
    this._onExit = function() {
        $("#verificationResult").text(getVerificationArray().toString());
    };

    function getVerificationArray() {
        var verification = [];
        
        if (_me.verified) {
            verification.push("Verified");
        }
        if (_me.incorrect) {
            verification.push("Incorrect");
        }
        if (_me.uncertain) {
            verification.push("Uncertain");
        }
        if (_me.unverified) {
            verification.push("Unverified");
        }
        
        return verification;
    }

    this.getJson = function() {
        return {
            verification: getVerificationArray()
        };
    };

    this.getError = function() {
        if (!(this.verified || this.incorrect || this.uncertain || this.unverified)) { return [ 'You must select at least one record verification status' ]; }
        return [];
    };
    
    function resetSelected(_me) {
        _me.verified = false;
        _me.incorrect = false;
        _me.uncertain = false;
        _me.unverified = false;
    }        
};
