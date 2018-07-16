/*
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the TU-Clausthal Mobile and Enterprise Computing Modeling     #
 * # Copyright (c) 2018-19                                                              #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 */
!function(e,t){"object"==typeof exports&&"object"==typeof module?module.exports=t():"function"==typeof define&&define.amd?define([],t):"object"==typeof exports?exports.AWN=t():e.AWN=t()}(window,function(){return function(e){var t={};function n(i){if(t[i])return t[i].exports;var o=t[i]={i:i,l:!1,exports:{}};return e[i].call(o.exports,o,o.exports,n),o.l=!0,o.exports}return n.m=e,n.c=t,n.d=function(e,t,i){n.o(e,t)||Object.defineProperty(e,t,{configurable:!1,enumerable:!0,get:i})},n.r=function(e){Object.defineProperty(e,"__esModule",{value:!0})},n.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return n.d(t,"a",t),t},n.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},n.p="",n(n.s=6)}([function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i={modal:"awn-modal",toast:"awn-toast",btn:"awn-btn",confirm:"awn-confirm"};t.tConsts={prefix:i.toast,klass:{label:i.toast+"-label",content:i.toast+"-content",icon:i.toast+"-icon",progressBar:i.toast+"-progress-bar",progressBarPause:i.toast+"-progress-bar-paused"},ids:{container:i.toast+"-container"}},t.mConsts={prefix:i.modal,klass:{buttons:"awn-buttons",button:i.btn,successBtn:i.btn+"-success",cancelBtn:i.btn+"-cancel",title:i.modal+"-title",body:i.modal+"-body",content:i.modal+"-content",dotAnimation:i.modal+"-loading-dots"},ids:{wrapper:i.modal+"-wrapper",confirmOk:i.confirm+"-ok",confirmCancel:i.confirm+"-cancel"}},t.eConsts={klass:{hiding:"awn-hiding"},lib:"awn"}},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}(),o=n(0);var r=function(){function e(t,n,i,o,r){var s=arguments.length>5&&void 0!==arguments[5]?arguments[5]:"div";!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e),this.newNode=document.createElement(s),n&&(this.newNode.id=n),i&&(this.newNode.className=i),r&&(this.newNode.innerHTML=r),o&&(this.newNode.style.cssText=o),this.parent=t,this.options={}}return i(e,[{key:"fire",value:function(e){return e?e.replace(this.newNode,this.type):this.insert()}},{key:"beforeInsert",value:function(){}},{key:"insert",value:function(){this.beforeInsert(),this.el=this.parent.appendChild(this.newNode),this.afterInsert()}},{key:"replace",value:function(e,t){var n=this;if(this.getElement())return this.beforeDelete().then(function(){n.type=t,n.parent.replaceChild(e,n.el),n.el=document.getElementById(e.id),n.afterInsert()})}},{key:"afterInsert",value:function(){}},{key:"beforeDelete",value:function(){var e=this,t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this.el;return new Promise(function(n,i){t.classList.add(o.eConsts.klass.hiding),setTimeout(n,e.options.animationDuration||300)})}},{key:"delete",value:function(){var e=this,t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this.el;return this.getElement(t)?this.beforeDelete(t).then(function(){return e.parent.removeChild(t)}):null}},{key:"getElement",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this.el;return document.getElementById(e.id)}},{key:"addEvent",value:function(e,t){this.el.addEventListener(e,t)}},{key:"addClass",value:function(e){this.el.classList.add(e)}},{key:"removeClass",value:function(e){this.el.classList.remove(e)}}]),e}();t.default=r},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i,o=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}(),r=n(1),s=(i=r)&&i.__esModule?i:{default:i},a=n(0);var u=function(e){function t(e,n,i){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t);var o=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,document.body,a.mConsts.ids.wrapper,null,"animation-duration: "+i.getSecs("animationDuration")+";"));return o.options=i,o.type=n,o.setInnerHtml(e),o.insert(),o}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,s.default),o(t,[{key:"setInnerHtml",value:function(e){var t=this.options.applyReplacements(e,this.type);switch(this.type){case"confirm":t="\n          "+this.options.icon(this.type)+"\n          <div class='"+a.mConsts.klass.title+"'>\n            "+this.options.label(this.type)+'\n          </div>\n          <div class="'+a.mConsts.klass.content+'">'+t+"</div>\n          <div class='"+a.mConsts.klass.buttons+"'>\n            <button class='"+a.mConsts.klass.button+" "+a.mConsts.klass.successBtn+"' id='"+a.mConsts.ids.confirmOk+"'>"+this.options.modal.okLabel+"</button>\n            <button class='"+a.mConsts.klass.button+" "+a.mConsts.klass.cancelBtn+"' id='"+a.mConsts.ids.confirmCancel+"'>"+this.options.modal.cancelLabel+"</button>\n          </div>\n       ";break;case"async-block":t=t+'<div class="'+a.mConsts.klass.dotAnimation+'"></div>'}this.newNode.innerHTML='\n      <div class="'+a.mConsts.klass.body+" "+a.mConsts.prefix+"-"+this.type+'" style="max-width: '+this.options.modal.maxWidth+';">\n        '+t+"\n      </div>\n     "}},{key:"hideAsync",value:function(e){var t=this,n=Date.now()-e;return new Promise(function(e,i){n>=t.options.asyncBlockMinDuration?(t.delete(),e()):setTimeout(function(){t.delete(),e()},t.options.asyncBlockMinDuration-n)})}}]),t}();t.default=u},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}();var o=function(){function e(t,n){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e),this.callback=t,this.remaining=n,this.resume()}return i(e,[{key:"pause",value:function(){window.clearTimeout(this.timerId),this.remaining-=new Date-this.start}},{key:"resume",value:function(){var e=this;this.start=new Date,window.clearTimeout(this.timerId),this.timerId=window.setTimeout(function(){window.clearTimeout(e.timerId),e.callback()},this.remaining)}}]),e}();t.default=o},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}(),o=a(n(1)),r=a(n(3)),s=n(0);function a(e){return e&&e.__esModule?e:{default:e}}var u=function(e){function t(e,n,i,o){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t);var r=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,o,s.tConsts.prefix+"-"+Math.floor(Date.now()-100*Math.random()),s.tConsts.prefix+" "+s.tConsts.prefix+"-"+n,"animation-duration: "+i.getSecs("animationDuration")+";"));return r.options=i,r.type=n,r.setInnerHtml(e),r}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,o.default),i(t,[{key:"setInnerHtml",value:function(e){e=this.options.applyReplacements(e,this.type);var t="";"async"!==this.type&&(t="<div class='"+s.tConsts.klass.progressBar+"' style=\"animation-duration:"+this.options.getSecs("duration")+';"></div>'),this.newNode.innerHTML="\n    "+t+"\n    "+this.getLabel()+'\n    <div class="'+s.tConsts.klass.content+'">'+e+'</div>\n    <span class="'+s.tConsts.klass.icon+'">'+this.options.icon(this.type)+"</span>\n    "}},{key:"beforeInsert",value:function(){var e=this;if(this.parent.childElementCount>=this.options.maxNotifications){var t=Array.from(this.parent.getElementsByClassName(s.tConsts.prefix));this.delete(t.find(function(t){return!e.isDeleted(t)}))}}},{key:"afterInsert",value:function(){var e=this;"async"!=this.type&&(this.timer=new r.default(function(){return e.delete()},this.options.duration),this.addEvent("click",function(){return e.delete()}),this.addEvent("mouseenter",function(){e.isDeleted()||(e.addClass(s.tConsts.klass.progressBarPause),e.timer.pause())}),this.addEvent("mouseleave",function(){e.isDeleted()||(e.removeClass(s.tConsts.klass.progressBarPause),e.timer.resume())}))}},{key:"isDeleted",value:function(){return(arguments.length>0&&void 0!==arguments[0]?arguments[0]:this.el).classList.contains(s.eConsts.klass.hiding)}},{key:"getLabel",value:function(){return'<b class="'+s.tConsts.klass.label+'">'+this.options.label(this.type)+"</b>"}}]),t}();t.default=u},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}(),o="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e};var r={labels:{tip:"Tip",info:"Info",success:"Success",warning:"Attention",alert:"Error",async:"Loading",confirm:"Confirmation required"},icons:{tip:"question-circle",info:"info-circle",success:"check-circle",warning:"exclamation-circle",alert:"warning",async:"cog fa-spin",confirm:"warning",prefix:"<i class='fa fa-fw fa-",suffix:"'></i>",enabled:!0},replacements:{tip:"",info:"",success:"",warning:"",alert:"",async:"","async-block":"",modal:"",confirm:"",general:{"<script>":"","<\/script>":""}},modal:{okLabel:"OK",cancelLabel:"Cancel",maxWidth:"500px"},messages:{async:"Please, wait...","async-block":"Loading"},handleReject:function(e){if("string"!=typeof e)throw Error("promise.reject() returning value should be a string, Given "+(void 0===e?"undefined":o(e))+" "+e);return e},maxNotifications:10,animationDuration:300,asyncBlockMinDuration:500,position:"bottom-right",duration:5e3},s=function(){function e(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e),Object.assign(this,function e(t,n){var i={};for(var r in t)n.hasOwnProperty(r)?"object"===o(t[r])?i[r]=e(t[r],n[r]):i[r]=n[r]:i[r]=t[r];return i}(r,t))}return i(e,[{key:"icon",value:function(e){return this.icons.enabled?""+this.icons.prefix+this.icons[e]+this.icons.suffix:""}},{key:"label",value:function(e){return this.labels[e]}},{key:"getSecs",value:function(e){return this[e]/1e3+"s"}},{key:"applyReplacements",value:function(e,t){if(!e)return this.messages[t]||"";for(var n in this.replacements.general)e=e.replace(n,this.replacements.general[n]);if(this.replacements[t])for(var i in this.replacements[t])e=e.replace(i,this.replacements[t][i]);return e}}]),e}();t.default=s},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},o=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}(),r=l(n(5)),s=l(n(4)),a=l(n(2)),u=l(n(1)),c=n(0);function l(e){return e&&e.__esModule?e:{default:e}}var f=function(){function e(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e),this.options=new r.default(t)}return o(e,[{key:"_err",value:function(e){throw Error(e)}},{key:"tip",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter");this.notify(e,"tip")}},{key:"info",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter");this.notify(e,"info")}},{key:"success",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter");this.notify(e,"success")}},{key:"warning",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter");this.notify(e,"warning")}},{key:"alert",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter");this.notify(e,"alert")}},{key:"async",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'promise' parameter"),t=arguments[1],n=this,i=arguments[2],o=arguments[3],r=this.notify(o,"async");return e.then(function(e){return n._runFunction(!0,t,e,r)},function(e){return Promise.reject(n._runFunction(!1,i,e,r))})}},{key:"notify",value:function(e,t,n){var i=new s.default(e,t,this.options,this._getContainer());return i.fire(n),i}},{key:"confirm",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter"),t=this,n=arguments[1],i=arguments[2],o=new a.default(e,"confirm",this.options);o.addEvent("click",function(e){if("BUTTON"!==e.target.nodeName)return!1;switch(o.delete(),e.target.id){case c.mConsts.ids.confirmOk:return t._runFunction(!0,n);case c.mConsts.ids.confirmCancel:return t._runFunction(!0,i)}})}},{key:"asyncBlock",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'promise' parameter"),t=arguments[1],n=this,i=arguments[2],o=arguments[3],r=new a.default(o,"async-block",this.options),s=Date.now();return e.then(function(e){return r.hideAsync(s).then(function(){return n._runFunction(!0,t,e)})},function(e){return r.hideAsync(s).then(function(){return Promise.reject(n._runFunction(!1,i,e))})})}},{key:"modal",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:this._err("missing 'html' parameter"),t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:this._err("missing className parameter"),n=new a.default(e,t,this.options);n.addEvent("click",function(e){e.target.id===n.el.id&&n.delete()})}},{key:"_getContainer",value:function(){return this.container||(this.container=document.getElementById(c.tConsts.ids.container)||this._createContainer()),this.container}},{key:"_createContainer",value:function(){var e=this.options.position.split("-"),t="top"===e[0]?c.eConsts.lib+"-top":"";"left"===e[1]&&(t=t+" "+c.eConsts.lib+"-left");var n=new u.default(document.body,c.tConsts.ids.container,t);return n.insert(),n.el}},{key:"_runFunction",value:function(e,t,n,o){switch(void 0===t?"undefined":i(t)){case"function":return o&&o.delete(),t(n);case"string":return this.notify(t,e?"success":"alert",o),n}return e?o&&o.delete():this.notify(this.options.handleReject(n),"alert",o),n}}]),e}();t.default=f}])});