if (typeof(Kumu) == 'undefined') {
    Kumu = {};
}
if (typeof(Kumu.Ajax) == 'undefined') {
    Kumu.Ajax = {};
};

Kumu.Ajax = {

    AJAX_COMPONENT_NAME : "AjaxComponent",
    HTTP_STATUS_OK : 200,
    HTTP_STATUS_NOT_FOUND : 404,

    XML_HTTP_REQUEST_STATUS_UNINITIALIZED : 0,
    XML_HTTP_REQUEST_STATUS_LOADING : 1,
    XML_HTTP_REQUEST_STATUS_LOADED : 2,
    XML_HTTP_REQUEST_STATUS_INTERACTIVE : 3,
    XML_HTTP_REQUEST_STATUS_COMPLETE : 4,
    
    AJAX_RESPONSE_TYPE_XML : 1,

    axo : new Array(
        "Microsoft.XMLHTTP",
        "Msxml2.XMLHTTP.4.0",
        "Msxml2.XMLHTTP.3.0",
        "Msxml2.XMLHTTP"
    ),

    DEBUG : false,
    
    getS2AjaxComponent : function() {
    	return new this.AjaxComponent();
    },
    
    AjaxComponent : function () {
    	var self = Kumu.Ajax;
        this.name = self.AJAX_COMPONENT_NAME;
	    this.responseType = null;
        this.url = "teeda.ajax";
        this.params = null;
        this.doAction = function(ajaxResponse){}
    },
    
    _createXmlHttp : function(){
        var xmlHttp = false;
        /*@cc_on
        @if (@_jscript_version >= 5)
        var self = Kumu.Ajax;
        for (var i = 0; !xmlHttp && i < self.axo.length; i++) {
            try {
                xmlHttp = new ActiveXObject(self.axo[i]);
            } catch(e) {
            }
        }
        @else
            xmlHttp = false;
        @end @*/
        if (!xmlHttp && typeof XMLHttpRequest != "undefined") {
            try{
                // for Firefox, safari
                xmlHttp = new XMLHttpRequest();
                //xmlHttp.overrideMimeType("text/xml");
            } catch(e) {
                xmlHttp = false;
            }
        }
        return xmlHttp
    },

    debugPrint : function(message, errorFlg) {
        if (errorFlg) {
            try {
                var div = document.createElement("div");
                document.body.appendChild(div);
                div.setAttribute("id", "ajax_msg");
                message = "<font color='red'>" + message + "</font>";
                document.getElementById("ajax_msg").innerHTML = "<br/>" + message;
            } catch (e) {
            }
        } else {
            try {
                var br = document.createElement("br");
                var span = document.createElement("span");
                document.body.appendChild(br);
                document.body.appendChild(span.appendChild(document.createTextNode(message)));
            } catch (e) {
            }
        }
    },
    
    _checkComponent : function(component) {
    	var self = Kumu.Ajax;
        var name;
        try {
            name = component.name;
        } catch(e) {
            return false;
        }
        if (self.AJAX_COMPONENT_NAME != name || !component.doAction || !component.url) {
            return false;
        }
        return true;
    },

    executeAjax : function(ajaxComponent) {
    	var self = Kumu.Ajax;
        if (!self._checkComponent(ajaxComponent)) {
            self.debugPrint("IllegalArgument. argument object is not AjaxComponent. implements url or doAction!", true);
            return;
        }
        
        var xmlHttp = self._createXmlHttp();
        if (!xmlHttp || !document.getElementById) {
            self.debugPrint("This browser does not support Ajax.", true);
            return;
        }
        
        var sysdate = new String(new Date());
        var url = ajaxComponent.url;
        
        var parameters = "";
        var params = ajaxComponent.params;
        var method = 'GET';
        if(params.method){
        	method = params.method.toUpperCase();
        	if(method != 'GET' && method != 'POST'){
        		method = 'GET';
        	}
        	delete params.method;
        }
		if(method == 'GET'){
            url += "?time=" + self.encodeURL(sysdate);
            if(null != params){
            
                for(var key in params){
                    parameters += "&" + key + "=" + self.encodeURL(params[key]);
                }
            }
            url += parameters;
            if(xmlHttp){
                self._registAjaxListener(xmlHttp, ajaxComponent);
                xmlHttp.open("GET", url, true);
                xmlHttp.setRequestHeader("If-Modified-Since", sysdate);
                xmlHttp.send(null);
            }
		}else{
			params['time'] = self.encodeURL(sysdate);
            if(params){
                var array = new Array();
                for(var v in params) {
                    array.push(v + "=" + encodeURIComponent(params[v]));
                }
                parameters = array.join("&");
            }
            if(xmlHttp){
                self._registAjaxListener(xmlHttp, ajaxComponent);
				xmlHttp.open("POST", url, true);
		        xmlHttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xmlHttp.setRequestHeader("If-Modified-Since", sysdate);
                xmlHttp.send(parameters);
            }
		}
    },

    _registAjaxListener : function(req, ajaxComponent) {
    	var self = Kumu.Ajax;
        req.onreadystatechange = function() {
            if (self.XML_HTTP_REQUEST_STATUS_COMPLETE == req.readyState) { 
                if (self.HTTP_STATUS_OK == req.status) {
                    if (self.DEBUG) self.debugPrint(req.responseText);
                    if (ajaxComponent.responseType) {
                        ajaxComponent.doAction(req.responseXML);
                    } else {
   	            	    ajaxComponent.doAction(req.responseText);
   	                }
			    } else {
        		    self.debugPrint("AjaxError! status["+req.status+"] message["+req.responseText+"]", true);
			    }
            }
        };
    },

    encodeURL : function encodeURL(val) {
        if (encodeURI) {
            return encodeURI(val);
        }
        if (encodeURIComponent) {
            return encodeURIComponent(val);
        }
        if (escape) {
            return escape(val);
        }        
    },    

    _getComponentName : function(func){
        var str = func.toString();
        var ret = str.match(/[0-9A-Za-z_]+\(/).toString();
        ret = ret.substring(0,ret.length-1); 
        var arr = ret.split('_');
        return arr;
    },
    
    executeTeedaAjax : function(callback, param, responseType){
    	var self = Kumu.Ajax;
        var ajax = self.getS2AjaxComponent();
        var components = self._getComponentName(callback);
        if(!param){
            param = {};
        }
        ajax.params = param;
        if(param instanceof Array){
            for(var i = 0; i < param.length; i++){
                ajax.params["AjaxParam" + new String(i)] = param[i];
            }
        }
        if(!("component" in param) && !("action" in param) && (components.length == 2) ){
            //callback name bind
            ajax.params["component"] = components[0];
            ajax.params["action"] = components[1];
        }
        
        
        ajax.doAction = callback;
        if(responseType != 'undefined'){
            ajax.responseType = responseType;
        }
        self.executeAjax(ajax);
    }
    
};