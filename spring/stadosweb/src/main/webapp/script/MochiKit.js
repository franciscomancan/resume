if(typeof (dojo)!="undefined"){
dojo.provide("MochiKit.Base");
}
if(typeof (MochiKit)=="undefined"){
MochiKit={};
}
if(typeof (MochiKit.Base)=="undefined"){
MochiKit.Base={};
}
if(typeof (MochiKit.__export__)=="undefined"){
MochiKit.__export__=(MochiKit.__compat__||(typeof (JSAN)=="undefined"&&typeof (dojo)=="undefined"));
}
MochiKit.Base.VERSION="1.4.2";
MochiKit.Base.NAME="MochiKit.Base";
MochiKit.Base.update=function(_1,_2){
if(_1===null||_1===undefined){
_1={};
}
for(var i=1;i<arguments.length;i++){
var o=arguments[i];
if(typeof (o)!="undefined"&&o!==null){
for(var k in o){
_1[k]=o[k];
}
}
}
return _1;
};
MochiKit.Base.update(MochiKit.Base,{__repr__:function(){
return "["+this.NAME+" "+this.VERSION+"]";
},toString:function(){
return this.__repr__();
},camelize:function(_6){
var _7=_6.split("-");
var cc=_7[0];
for(var i=1;i<_7.length;i++){
cc+=_7[i].charAt(0).toUpperCase()+_7[i].substring(1);
}
return cc;
},counter:function(n){
if(arguments.length===0){
n=1;
}
return function(){
return n++;
};
},clone:function(_b){
var me=arguments.callee;
if(arguments.length==1){
me.prototype=_b;
return new me();
}
},_deps:function(_d,_e){
if(!(_d in MochiKit)){
MochiKit[_d]={};
}
if(typeof (dojo)!="undefined"){
dojo.provide("MochiKit."+_d);
}
for(var i=0;i<_e.length;i++){
if(typeof (dojo)!="undefined"){
dojo.require("MochiKit."+_e[i]);
}
if(typeof (JSAN)!="undefined"){
JSAN.use("MochiKit."+_e[i],[]);
}
if(!(_e[i] in MochiKit)){
throw "MochiKit."+_d+" depends on MochiKit."+_e[i]+"!";
}
}
},_flattenArray:function(res,lst){
for(var i=0;i<lst.length;i++){
var o=lst[i];
if(o instanceof Array){
arguments.callee(res,o);
}else{
res.push(o);
}
}
return res;
},flattenArray:function(lst){
return MochiKit.Base._flattenArray([],lst);
},flattenArguments:function(lst){
var res=[];
var m=MochiKit.Base;
var _18=m.extend(null,arguments);
while(_18.length){
var o=_18.shift();
if(o&&typeof (o)=="object"&&typeof (o.length)=="number"){
for(var i=o.length-1;i>=0;i--){
_18.unshift(o[i]);
}
}else{
res.push(o);
}
}
return res;
},extend:function(_1b,obj,_1d){
if(!_1d){
_1d=0;
}
if(obj){
var l=obj.length;
if(typeof (l)!="number"){
if(typeof (MochiKit.Iter)!="undefined"){
obj=MochiKit.Iter.list(obj);
l=obj.length;
}else{
throw new TypeError("Argument not an array-like and MochiKit.Iter not present");
}
}
if(!_1b){
_1b=[];
}
for(var i=_1d;i<l;i++){
_1b.push(obj[i]);
}
}
return _1b;
},updatetree:function(_20,obj){
if(_20===null||_20===undefined){
_20={};
}
for(var i=1;i<arguments.length;i++){
var o=arguments[i];
if(typeof (o)!="undefined"&&o!==null){
for(var k in o){
var v=o[k];
if(typeof (_20[k])=="object"&&typeof (v)=="object"){
arguments.callee(_20[k],v);
}else{
_20[k]=v;
}
}
}
}
return _20;
},setdefault:function(_26,obj){
if(_26===null||_26===undefined){
_26={};
}
for(var i=1;i<arguments.length;i++){
var o=arguments[i];
for(var k in o){
if(!(k in _26)){
_26[k]=o[k];
}
}
}
return _26;
},keys:function(obj){
var _2c=[];
for(var _2d in obj){
_2c.push(_2d);
}
return _2c;
},values:function(obj){
var _2f=[];
for(var _30 in obj){
_2f.push(obj[_30]);
}
return _2f;
},items:function(obj){
var _32=[];
var e;
for(var _34 in obj){
var v;
try{
v=obj[_34];
}
catch(e){
continue;
}
_32.push([_34,v]);
}
return _32;
},_newNamedError:function(_36,_37,_38){
_38.prototype=new MochiKit.Base.NamedError(_36.NAME+"."+_37);
_36[_37]=_38;
},operator:{truth:function(a){
return !!a;
},lognot:function(a){
return !a;
},identity:function(a){
return a;
},not:function(a){
return ~a;
},neg:function(a){
return -a;
},add:function(a,b){
return a+b;
},sub:function(a,b){
return a-b;
},div:function(a,b){
return a/b;
},mod:function(a,b){
return a%b;
},mul:function(a,b){
return a*b;
},and:function(a,b){
return a&b;
},or:function(a,b){
return a|b;
},xor:function(a,b){
return a^b;
},lshift:function(a,b){
return a<<b;
},rshift:function(a,b){
return a>>b;
},zrshift:function(a,b){
return a>>>b;
},eq:function(a,b){
return a==b;
},ne:function(a,b){
return a!=b;
},gt:function(a,b){
return a>b;
},ge:function(a,b){
return a>=b;
},lt:function(a,b){
return a<b;
},le:function(a,b){
return a<=b;
},seq:function(a,b){
return a===b;
},sne:function(a,b){
return a!==b;
},ceq:function(a,b){
return MochiKit.Base.compare(a,b)===0;
},cne:function(a,b){
return MochiKit.Base.compare(a,b)!==0;
},cgt:function(a,b){
return MochiKit.Base.compare(a,b)==1;
},cge:function(a,b){
return MochiKit.Base.compare(a,b)!=-1;
},clt:function(a,b){
return MochiKit.Base.compare(a,b)==-1;
},cle:function(a,b){
return MochiKit.Base.compare(a,b)!=1;
},logand:function(a,b){
return a&&b;
},logor:function(a,b){
return a||b;
},contains:function(a,b){
return b in a;
}},forwardCall:function(_76){
return function(){
return this[_76].apply(this,arguments);
};
},itemgetter:function(_77){
return function(arg){
return arg[_77];
};
},typeMatcher:function(){
var _79={};
for(var i=0;i<arguments.length;i++){
var typ=arguments[i];
_79[typ]=typ;
}
return function(){
for(var i=0;i<arguments.length;i++){
if(!(typeof (arguments[i]) in _79)){
return false;
}
}
return true;
};
},isNull:function(){
for(var i=0;i<arguments.length;i++){
if(arguments[i]!==null){
return false;
}
}
return true;
},isUndefinedOrNull:function(){
for(var i=0;i<arguments.length;i++){
var o=arguments[i];
if(!(typeof (o)=="undefined"||o===null)){
return false;
}
}
return true;
},isEmpty:function(obj){
return !MochiKit.Base.isNotEmpty.apply(this,arguments);
},isNotEmpty:function(obj){
for(var i=0;i<arguments.length;i++){
var o=arguments[i];
if(!(o&&o.length)){
return false;
}
}
return true;
},isArrayLike:function(){
for(var i=0;i<arguments.length;i++){
var o=arguments[i];
var typ=typeof (o);
if((typ!="object"&&!(typ=="function"&&typeof (o.item)=="function"))||o===null||typeof (o.length)!="number"||o.nodeType===3||o.nodeType===4){
return false;
}
}
return true;
},isDateLike:function(){
for(var i=0;i<arguments.length;i++){
var o=arguments[i];
if(typeof (o)!="object"||o===null||typeof (o.getTime)!="function"){
return false;
}
}
return true;
},xmap:function(fn){
if(fn===null){
return MochiKit.Base.extend(null,arguments,1);
}
var _8a=[];
for(var i=1;i<arguments.length;i++){
_8a.push(fn(arguments[i]));
}
return _8a;
},map:function(fn,lst){
var m=MochiKit.Base;
var itr=MochiKit.Iter;
var _90=m.isArrayLike;
if(arguments.length<=2){
if(!_90(lst)){
if(itr){
lst=itr.list(lst);
if(fn===null){
return lst;
}
}else{
throw new TypeError("Argument not an array-like and MochiKit.Iter not present");
}
}
if(fn===null){
return m.extend(null,lst);
}
var _91=[];
for(var i=0;i<lst.length;i++){
_91.push(fn(lst[i]));
}
return _91;
}else{
if(fn===null){
fn=Array;
}
var _93=null;
for(i=1;i<arguments.length;i++){
if(!_90(arguments[i])){
if(itr){
return itr.list(itr.imap.apply(null,arguments));
}else{
throw new TypeError("Argument not an array-like and MochiKit.Iter not present");
}
}
var l=arguments[i].length;
if(_93===null||_93>l){
_93=l;
}
}
_91=[];
for(i=0;i<_93;i++){
var _95=[];
for(var j=1;j<arguments.length;j++){
_95.push(arguments[j][i]);
}
_91.push(fn.apply(this,_95));
}
return _91;
}
},xfilter:function(fn){
var _98=[];
if(fn===null){
fn=MochiKit.Base.operator.truth;
}
for(var i=1;i<arguments.length;i++){
var o=arguments[i];
if(fn(o)){
_98.push(o);
}
}
return _98;
},filter:function(fn,lst,_9d){
var _9e=[];
var m=MochiKit.Base;
if(!m.isArrayLike(lst)){
if(MochiKit.Iter){
lst=MochiKit.Iter.list(lst);
}else{
throw new TypeError("Argument not an array-like and MochiKit.Iter not present");
}
}
if(fn===null){
fn=m.operator.truth;
}
if(typeof (Array.prototype.filter)=="function"){
return Array.prototype.filter.call(lst,fn,_9d);
}else{
if(typeof (_9d)=="undefined"||_9d===null){
for(var i=0;i<lst.length;i++){
var o=lst[i];
if(fn(o)){
_9e.push(o);
}
}
}else{
for(i=0;i<lst.length;i++){
o=lst[i];
if(fn.call(_9d,o)){
_9e.push(o);
}
}
}
}
return _9e;
},_wrapDumbFunction:function(_a2){
return function(){
switch(arguments.length){
case 0:
return _a2();
case 1:
return _a2(arguments[0]);
case 2:
return _a2(arguments[0],arguments[1]);
case 3:
return _a2(arguments[0],arguments[1],arguments[2]);
}
var _a3=[];
for(var i=0;i<arguments.length;i++){
_a3.push("arguments["+i+"]");
}
return eval("(func("+_a3.join(",")+"))");
};
},methodcaller:function(_a5){
var _a6=MochiKit.Base.extend(null,arguments,1);
if(typeof (_a5)=="function"){
return function(obj){
return _a5.apply(obj,_a6);
};
}else{
return function(obj){
return obj[_a5].apply(obj,_a6);
};
}
},method:function(_a9,_aa){
var m=MochiKit.Base;
return m.bind.apply(this,m.extend([_aa,_a9],arguments,2));
},compose:function(f1,f2){
var _ae=[];
var m=MochiKit.Base;
if(arguments.length===0){
throw new TypeError("compose() requires at least one argument");
}
for(var i=0;i<arguments.length;i++){
var fn=arguments[i];
if(typeof (fn)!="function"){
throw new TypeError(m.repr(fn)+" is not a function");
}
_ae.push(fn);
}
return function(){
var _b2=arguments;
for(var i=_ae.length-1;i>=0;i--){
_b2=[_ae[i].apply(this,_b2)];
}
return _b2[0];
};
},bind:function(_b4,_b5){
if(typeof (_b4)=="string"){
_b4=_b5[_b4];
}
var _b6=_b4.im_func;
var _b7=_b4.im_preargs;
var _b8=_b4.im_self;
var m=MochiKit.Base;
if(typeof (_b4)=="function"&&typeof (_b4.apply)=="undefined"){
_b4=m._wrapDumbFunction(_b4);
}
if(typeof (_b6)!="function"){
_b6=_b4;
}
if(typeof (_b5)!="undefined"){
_b8=_b5;
}
if(typeof (_b7)=="undefined"){
_b7=[];
}else{
_b7=_b7.slice();
}
m.extend(_b7,arguments,2);
var _ba=function(){
var _bb=arguments;
var me=arguments.callee;
if(me.im_preargs.length>0){
_bb=m.concat(me.im_preargs,_bb);
}
var _bd=me.im_self;
if(!_bd){
_bd=this;
}
return me.im_func.apply(_bd,_bb);
};
_ba.im_self=_b8;
_ba.im_func=_b6;
_ba.im_preargs=_b7;
return _ba;
},bindLate:function(_be,_bf){
var m=MochiKit.Base;
if(typeof (_be)!="string"){
return m.bind.apply(this,arguments);
}
var _c1=m.extend([],arguments,2);
var _c2=function(){
var _c3=arguments;
var me=arguments.callee;
if(me.im_preargs.length>0){
_c3=m.concat(me.im_preargs,_c3);
}
var _c5=me.im_self;
if(!_c5){
_c5=this;
}
return _c5[me.im_func].apply(_c5,_c3);
};
_c2.im_self=_bf;
_c2.im_func=_be;
_c2.im_preargs=_c1;
return _c2;
},bindMethods:function(_c6){
var _c7=MochiKit.Base.bind;
for(var k in _c6){
var _c9=_c6[k];
if(typeof (_c9)=="function"){
_c6[k]=_c7(_c9,_c6);
}
}
},registerComparator:function(_ca,_cb,_cc,_cd){
MochiKit.Base.comparatorRegistry.register(_ca,_cb,_cc,_cd);
},_primitives:{"boolean":true,"string":true,"number":true},compare:function(a,b){
if(a==b){
return 0;
}
var _d0=(typeof (a)=="undefined"||a===null);
var _d1=(typeof (b)=="undefined"||b===null);
if(_d0&&_d1){
return 0;
}else{
if(_d0){
return -1;
}else{
if(_d1){
return 1;
}
}
}
var m=MochiKit.Base;
var _d3=m._primitives;
if(!(typeof (a) in _d3&&typeof (b) in _d3)){
try{
return m.comparatorRegistry.match(a,b);
}
catch(e){
if(e!=m.NotFound){
throw e;
}
}
}
if(a<b){
return -1;
}else{
if(a>b){
return 1;
}
}
var _d4=m.repr;
throw new TypeError(_d4(a)+" and "+_d4(b)+" can not be compared");
},compareDateLike:function(a,b){
return MochiKit.Base.compare(a.getTime(),b.getTime());
},compareArrayLike:function(a,b){
var _d9=MochiKit.Base.compare;
var _da=a.length;
var _db=0;
if(_da>b.length){
_db=1;
_da=b.length;
}else{
if(_da<b.length){
_db=-1;
}
}
for(var i=0;i<_da;i++){
var cmp=_d9(a[i],b[i]);
if(cmp){
return cmp;
}
}
return _db;
},registerRepr:function(_de,_df,_e0,_e1){
MochiKit.Base.reprRegistry.register(_de,_df,_e0,_e1);
},repr:function(o){
if(typeof (o)=="undefined"){
return "undefined";
}else{
if(o===null){
return "null";
}
}
try{
if(typeof (o.__repr__)=="function"){
return o.__repr__();
}else{
if(typeof (o.repr)=="function"&&o.repr!=arguments.callee){
return o.repr();
}
}
return MochiKit.Base.reprRegistry.match(o);
}
catch(e){
if(typeof (o.NAME)=="string"&&(o.toString==Function.prototype.toString||o.toString==Object.prototype.toString)){
return o.NAME;
}
}
try{
var _e3=(o+"");
}
catch(e){
return "["+typeof (o)+"]";
}
if(typeof (o)=="function"){
_e3=_e3.replace(/^\s+/,"").replace(/\s+/g," ");
_e3=_e3.replace(/,(\S)/,", $1");
var idx=_e3.indexOf("{");
if(idx!=-1){
_e3=_e3.substr(0,idx)+"{...}";
}
}
return _e3;
},reprArrayLike:function(o){
var m=MochiKit.Base;
return "["+m.map(m.repr,o).join(", ")+"]";
},reprString:function(o){
return ("\""+o.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\v]/g,"\\v").replace(/[\r]/g,"\\r");
},reprNumber:function(o){
return o+"";
},registerJSON:function(_e9,_ea,_eb,_ec){
MochiKit.Base.jsonRegistry.register(_e9,_ea,_eb,_ec);
},evalJSON:function(){
return eval("("+MochiKit.Base._filterJSON(arguments[0])+")");
},_filterJSON:function(s){
var m=s.match(/^\s*\/\*(.*)\*\/\s*$/);
if(m){
return m[1];
}
return s;
},serializeJSON:function(o){
var _f0=typeof (o);
if(_f0=="number"||_f0=="boolean"){
return o+"";
}else{
if(o===null){
return "null";
}else{
if(_f0=="string"){
var res="";
for(var i=0;i<o.length;i++){
var c=o.charAt(i);
if(c=="\""){
res+="\\\"";
}else{
if(c=="\\"){
res+="\\\\";
}else{
if(c=="\b"){
res+="\\b";
}else{
if(c=="\f"){
res+="\\f";
}else{
if(c=="\n"){
res+="\\n";
}else{
if(c=="\r"){
res+="\\r";
}else{
if(c=="\t"){
res+="\\t";
}else{
if(o.charCodeAt(i)<=31){
var hex=o.charCodeAt(i).toString(16);
if(hex.length<2){
hex="0"+hex;
}
res+="\\u00"+hex.toUpperCase();
}else{
res+=c;
}
}
}
}
}
}
}
}
}
return "\""+res+"\"";
}
}
}
var me=arguments.callee;
var _f6;
if(typeof (o.__json__)=="function"){
_f6=o.__json__();
if(o!==_f6){
return me(_f6);
}
}
if(typeof (o.json)=="function"){
_f6=o.json();
if(o!==_f6){
return me(_f6);
}
}
if(_f0!="function"&&typeof (o.length)=="number"){
var res=[];
for(var i=0;i<o.length;i++){
var val=me(o[i]);
if(typeof (val)!="string"){
continue;
}
res.push(val);
}
return "["+res.join(", ")+"]";
}
var m=MochiKit.Base;
try{
_f6=m.jsonRegistry.match(o);
if(o!==_f6){
return me(_f6);
}
}
catch(e){
if(e!=m.NotFound){
throw e;
}
}
if(_f0=="undefined"){
throw new TypeError("undefined can not be serialized as JSON");
}
if(_f0=="function"){
return null;
}
res=[];
for(var k in o){
var _fa;
if(typeof (k)=="number"){
_fa="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_fa=me(k);
}else{
continue;
}
}
val=me(o[k]);
if(typeof (val)!="string"){
continue;
}
res.push(_fa+":"+val);
}
return "{"+res.join(", ")+"}";
},objEqual:function(a,b){
return (MochiKit.Base.compare(a,b)===0);
},arrayEqual:function(_fd,arr){
if(_fd.length!=arr.length){
return false;
}
return (MochiKit.Base.compare(_fd,arr)===0);
},concat:function(){
var _ff=[];
var _100=MochiKit.Base.extend;
for(var i=0;i<arguments.length;i++){
_100(_ff,arguments[i]);
}
return _ff;
},keyComparator:function(key){
var m=MochiKit.Base;
var _104=m.compare;
if(arguments.length==1){
return function(a,b){
return _104(a[key],b[key]);
};
}
var _107=m.extend(null,arguments);
return function(a,b){
var rval=0;
for(var i=0;(rval===0)&&(i<_107.length);i++){
var key=_107[i];
rval=_104(a[key],b[key]);
}
return rval;
};
},reverseKeyComparator:function(key){
var _10e=MochiKit.Base.keyComparator.apply(this,arguments);
return function(a,b){
return _10e(b,a);
};
},partial:function(func){
var m=MochiKit.Base;
return m.bind.apply(this,m.extend([func,undefined],arguments,1));
},listMinMax:function(_113,lst){
if(lst.length===0){
return null;
}
var cur=lst[0];
var _116=MochiKit.Base.compare;
for(var i=1;i<lst.length;i++){
var o=lst[i];
if(_116(o,cur)==_113){
cur=o;
}
}
return cur;
},objMax:function(){
return MochiKit.Base.listMinMax(1,arguments);
},objMin:function(){
return MochiKit.Base.listMinMax(-1,arguments);
},findIdentical:function(lst,_11a,_11b,end){
if(typeof (end)=="undefined"||end===null){
end=lst.length;
}
if(typeof (_11b)=="undefined"||_11b===null){
_11b=0;
}
for(var i=_11b;i<end;i++){
if(lst[i]===_11a){
return i;
}
}
return -1;
},mean:function(){
var sum=0;
var m=MochiKit.Base;
var args=m.extend(null,arguments);
var _121=args.length;
while(args.length){
var o=args.shift();
if(o&&typeof (o)=="object"&&typeof (o.length)=="number"){
_121+=o.length-1;
for(var i=o.length-1;i>=0;i--){
sum+=o[i];
}
}else{
sum+=o;
}
}
if(_121<=0){
throw new TypeError("mean() requires at least one argument");
}
return sum/_121;
},median:function(){
var data=MochiKit.Base.flattenArguments(arguments);
if(data.length===0){
throw new TypeError("median() requires at least one argument");
}
data.sort(compare);
if(data.length%2==0){
var _125=data.length/2;
return (data[_125]+data[_125-1])/2;
}else{
return data[(data.length-1)/2];
}
},findValue:function(lst,_127,_128,end){
if(typeof (end)=="undefined"||end===null){
end=lst.length;
}
if(typeof (_128)=="undefined"||_128===null){
_128=0;
}
var cmp=MochiKit.Base.compare;
for(var i=_128;i<end;i++){
if(cmp(lst[i],_127)===0){
return i;
}
}
return -1;
},nodeWalk:function(node,_12d){
var _12e=[node];
var _12f=MochiKit.Base.extend;
while(_12e.length){
var res=_12d(_12e.shift());
if(res){
_12f(_12e,res);
}
}
},nameFunctions:function(_131){
var base=_131.NAME;
if(typeof (base)=="undefined"){
base="";
}else{
base=base+".";
}
for(var name in _131){
var o=_131[name];
if(typeof (o)=="function"&&typeof (o.NAME)=="undefined"){
try{
o.NAME=base+name;
}
catch(e){
}
}
}
},queryString:function(_135,_136){
if(typeof (MochiKit.DOM)!="undefined"&&arguments.length==1&&(typeof (_135)=="string"||(typeof (_135.nodeType)!="undefined"&&_135.nodeType>0))){
var kv=MochiKit.DOM.formContents(_135);
_135=kv[0];
_136=kv[1];
}else{
if(arguments.length==1){
if(typeof (_135.length)=="number"&&_135.length==2){
return arguments.callee(_135[0],_135[1]);
}
var o=_135;
_135=[];
_136=[];
for(var k in o){
var v=o[k];
if(typeof (v)=="function"){
continue;
}else{
if(MochiKit.Base.isArrayLike(v)){
for(var i=0;i<v.length;i++){
_135.push(k);
_136.push(v[i]);
}
}else{
_135.push(k);
_136.push(v);
}
}
}
}
}
var rval=[];
var len=Math.min(_135.length,_136.length);
var _13e=MochiKit.Base.urlEncode;
for(var i=0;i<len;i++){
v=_136[i];
if(typeof (v)!="undefined"&&v!==null){
rval.push(_13e(_135[i])+"="+_13e(v));
}
}
return rval.join("&");
},parseQueryString:function(_13f,_140){
var qstr=(_13f.charAt(0)=="?")?_13f.substring(1):_13f;
var _142=qstr.replace(/\+/g,"%20").split(/\&amp\;|\&\#38\;|\&#x26;|\&/);
var o={};
var _144;
if(typeof (decodeURIComponent)!="undefined"){
_144=decodeURIComponent;
}else{
_144=unescape;
}
if(_140){
for(var i=0;i<_142.length;i++){
var pair=_142[i].split("=");
var name=_144(pair.shift());
if(!name){
continue;
}
var arr=o[name];
if(!(arr instanceof Array)){
arr=[];
o[name]=arr;
}
arr.push(_144(pair.join("=")));
}
}else{
for(i=0;i<_142.length;i++){
pair=_142[i].split("=");
var name=pair.shift();
if(!name){
continue;
}
o[_144(name)]=_144(pair.join("="));
}
}
return o;
}});
MochiKit.Base.AdapterRegistry=function(){
this.pairs=[];
};
MochiKit.Base.AdapterRegistry.prototype={register:function(name,_14a,wrap,_14c){
if(_14c){
this.pairs.unshift([name,_14a,wrap]);
}else{
this.pairs.push([name,_14a,wrap]);
}
},match:function(){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[1].apply(this,arguments)){
return pair[2].apply(this,arguments);
}
}
throw MochiKit.Base.NotFound;
},unregister:function(name){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[0]==name){
this.pairs.splice(i,1);
return true;
}
}
return false;
}};
MochiKit.Base.EXPORT=["flattenArray","noop","camelize","counter","clone","extend","update","updatetree","setdefault","keys","values","items","NamedError","operator","forwardCall","itemgetter","typeMatcher","isCallable","isUndefined","isUndefinedOrNull","isNull","isEmpty","isNotEmpty","isArrayLike","isDateLike","xmap","map","xfilter","filter","methodcaller","compose","bind","bindLate","bindMethods","NotFound","AdapterRegistry","registerComparator","compare","registerRepr","repr","objEqual","arrayEqual","concat","keyComparator","reverseKeyComparator","partial","merge","listMinMax","listMax","listMin","objMax","objMin","nodeWalk","zip","urlEncode","queryString","serializeJSON","registerJSON","evalJSON","parseQueryString","findValue","findIdentical","flattenArguments","method","average","mean","median"];
MochiKit.Base.EXPORT_OK=["nameFunctions","comparatorRegistry","reprRegistry","jsonRegistry","compareDateLike","compareArrayLike","reprArrayLike","reprString","reprNumber"];
MochiKit.Base._exportSymbols=function(_152,_153){
if(!MochiKit.__export__){
return;
}
var all=_153.EXPORT_TAGS[":all"];
for(var i=0;i<all.length;i++){
_152[all[i]]=_153[all[i]];
}
};
MochiKit.Base.__new__=function(){
var m=this;
m.noop=m.operator.identity;
m.forward=m.forwardCall;
m.find=m.findValue;
if(typeof (encodeURIComponent)!="undefined"){
m.urlEncode=function(_157){
return encodeURIComponent(_157).replace(/\'/g,"%27");
};
}else{
m.urlEncode=function(_158){
return escape(_158).replace(/\+/g,"%2B").replace(/\"/g,"%22").rval.replace(/\'/g,"%27");
};
}
m.NamedError=function(name){
this.message=name;
this.name=name;
};
m.NamedError.prototype=new Error();
m.update(m.NamedError.prototype,{repr:function(){
if(this.message&&this.message!=this.name){
return this.name+"("+m.repr(this.message)+")";
}else{
return this.name+"()";
}
},toString:m.forwardCall("repr")});
m.NotFound=new m.NamedError("MochiKit.Base.NotFound");
m.listMax=m.partial(m.listMinMax,1);
m.listMin=m.partial(m.listMinMax,-1);
m.isCallable=m.typeMatcher("function");
m.isUndefined=m.typeMatcher("undefined");
m.merge=m.partial(m.update,null);
m.zip=m.partial(m.map,null);
m.average=m.mean;
m.comparatorRegistry=new m.AdapterRegistry();
m.registerComparator("dateLike",m.isDateLike,m.compareDateLike);
m.registerComparator("arrayLike",m.isArrayLike,m.compareArrayLike);
m.reprRegistry=new m.AdapterRegistry();
m.registerRepr("arrayLike",m.isArrayLike,m.reprArrayLike);
m.registerRepr("string",m.typeMatcher("string"),m.reprString);
m.registerRepr("numbers",m.typeMatcher("number","boolean"),m.reprNumber);
m.jsonRegistry=new m.AdapterRegistry();
var all=m.concat(m.EXPORT,m.EXPORT_OK);
m.EXPORT_TAGS={":common":m.concat(m.EXPORT_OK),":all":all};
m.nameFunctions(this);
};
MochiKit.Base.__new__();
if(MochiKit.__export__){
compare=MochiKit.Base.compare;
compose=MochiKit.Base.compose;
serializeJSON=MochiKit.Base.serializeJSON;
mean=MochiKit.Base.mean;
median=MochiKit.Base.median;
}
MochiKit.Base._exportSymbols(this,MochiKit.Base);
MochiKit.Base._deps("Async",["Base"]);
MochiKit.Async.NAME="MochiKit.Async";
MochiKit.Async.VERSION="1.4.2";
MochiKit.Async.__repr__=function(){
return "["+this.NAME+" "+this.VERSION+"]";
};
MochiKit.Async.toString=function(){
return this.__repr__();
};
MochiKit.Async.Deferred=function(_28d){
this.chain=[];
this.id=this._nextId();
this.fired=-1;
this.paused=0;
this.results=[null,null];
this.canceller=_28d;
this.silentlyCancelled=false;
this.chained=false;
};
MochiKit.Async.Deferred.prototype={repr:function(){
var _28e;
if(this.fired==-1){
_28e="unfired";
}else{
if(this.fired===0){
_28e="success";
}else{
_28e="error";
}
}
return "Deferred("+this.id+", "+_28e+")";
},toString:MochiKit.Base.forwardCall("repr"),_nextId:MochiKit.Base.counter(),cancel:function(){
var self=MochiKit.Async;
if(this.fired==-1){
if(this.canceller){
this.canceller(this);
}else{
this.silentlyCancelled=true;
}
if(this.fired==-1){
this.errback(new self.CancelledError(this));
}
}else{
if((this.fired===0)&&(this.results[0] instanceof self.Deferred)){
this.results[0].cancel();
}
}
},_resback:function(res){
this.fired=((res instanceof Error)?1:0);
this.results[this.fired]=res;
this._fire();
},_check:function(){
if(this.fired!=-1){
if(!this.silentlyCancelled){
throw new MochiKit.Async.AlreadyCalledError(this);
}
this.silentlyCancelled=false;
return;
}
},callback:function(res){
this._check();
if(res instanceof MochiKit.Async.Deferred){
throw new Error("Deferred instances can only be chained if they are the result of a callback");
}
this._resback(res);
},errback:function(res){
this._check();
var self=MochiKit.Async;
if(res instanceof self.Deferred){
throw new Error("Deferred instances can only be chained if they are the result of a callback");
}
if(!(res instanceof Error)){
res=new self.GenericError(res);
}
this._resback(res);
},addBoth:function(fn){
if(arguments.length>1){
fn=MochiKit.Base.partial.apply(null,arguments);
}
return this.addCallbacks(fn,fn);
},addCallback:function(fn){
if(arguments.length>1){
fn=MochiKit.Base.partial.apply(null,arguments);
}
return this.addCallbacks(fn,null);
},addErrback:function(fn){
if(arguments.length>1){
fn=MochiKit.Base.partial.apply(null,arguments);
}
return this.addCallbacks(null,fn);
},addCallbacks:function(cb,eb){
if(this.chained){
throw new Error("Chained Deferreds can not be re-used");
}
this.chain.push([cb,eb]);
if(this.fired>=0){
this._fire();
}
return this;
},_fire:function(){
var _299=this.chain;
var _29a=this.fired;
var res=this.results[_29a];
var self=this;
var cb=null;
while(_299.length>0&&this.paused===0){
var pair=_299.shift();
var f=pair[_29a];
if(f===null){
continue;
}
try{
res=f(res);
_29a=((res instanceof Error)?1:0);
if(res instanceof MochiKit.Async.Deferred){
cb=function(res){
self._resback(res);
self.paused--;
if((self.paused===0)&&(self.fired>=0)){
self._fire();
}
};
this.paused++;
}
}
catch(err){
_29a=1;
if(!(err instanceof Error)){
err=new MochiKit.Async.GenericError(err);
}
res=err;
}
}
this.fired=_29a;
this.results[_29a]=res;
if(cb&&this.paused){
res.addBoth(cb);
res.chained=true;
}
}};
MochiKit.Base.update(MochiKit.Async,{evalJSONRequest:function(req){
return MochiKit.Base.evalJSON(req.responseText);
},succeed:function(_2a2){
var d=new MochiKit.Async.Deferred();
d.callback.apply(d,arguments);
return d;
},fail:function(_2a4){
var d=new MochiKit.Async.Deferred();
d.errback.apply(d,arguments);
return d;
},getXMLHttpRequest:function(){
var self=arguments.callee;
if(!self.XMLHttpRequest){
var _2a7=[function(){
return new XMLHttpRequest();
},function(){
return new ActiveXObject("Msxml2.XMLHTTP");
},function(){
return new ActiveXObject("Microsoft.XMLHTTP");
},function(){
return new ActiveXObject("Msxml2.XMLHTTP.4.0");
},function(){
throw new MochiKit.Async.BrowserComplianceError("Browser does not support XMLHttpRequest");
}];
for(var i=0;i<_2a7.length;i++){
var func=_2a7[i];
try{
self.XMLHttpRequest=func;
return func();
}
catch(e){
}
}
}
return self.XMLHttpRequest();
},_xhr_onreadystatechange:function(d){
var m=MochiKit.Base;
if(this.readyState==4){
try{
this.onreadystatechange=null;
}
catch(e){
try{
this.onreadystatechange=m.noop;
}
catch(e){
}
}
var _2ac=null;
try{
_2ac=this.status;
if(!_2ac&&m.isNotEmpty(this.responseText)){
_2ac=304;
}
}
catch(e){
}
if(_2ac==200||_2ac==201||_2ac==204||_2ac==304||_2ac==1223){
d.callback(this);
}else{
var err=new MochiKit.Async.XMLHttpRequestError(this,"Request failed");
if(err.number){
d.errback(err);
}else{
d.errback(err);
}
}
}
},_xhr_canceller:function(req){
try{
req.onreadystatechange=null;
}
catch(e){
try{
req.onreadystatechange=MochiKit.Base.noop;
}
catch(e){
}
}
req.abort();
},sendXMLHttpRequest:function(req,_2b0){
if(typeof (_2b0)=="undefined"||_2b0===null){
_2b0="";
}
var m=MochiKit.Base;
var self=MochiKit.Async;
var d=new self.Deferred(m.partial(self._xhr_canceller,req));
try{
req.onreadystatechange=m.bind(self._xhr_onreadystatechange,req,d);
req.send(_2b0);
}
catch(e){
try{
req.onreadystatechange=null;
}
catch(ignore){
}
d.errback(e);
}
return d;
},doXHR:function(url,opts){
var self=MochiKit.Async;
return self.callLater(0,self._doXHR,url,opts);
},_doXHR:function(url,opts){
var m=MochiKit.Base;
opts=m.update({method:"GET",sendContent:""},opts);
var self=MochiKit.Async;
var req=self.getXMLHttpRequest();
if(opts.queryString){
var qs=m.queryString(opts.queryString);
if(qs){
url+="?"+qs;
}
}
if("username" in opts){
req.open(opts.method,url,true,opts.username,opts.password);
}else{
req.open(opts.method,url,true);
}
if(req.overrideMimeType&&opts.mimeType){
req.overrideMimeType(opts.mimeType);
}
req.setRequestHeader("X-Requested-With","XMLHttpRequest");
if(opts.headers){
var _2bd=opts.headers;
if(!m.isArrayLike(_2bd)){
_2bd=m.items(_2bd);
}
for(var i=0;i<_2bd.length;i++){
var _2bf=_2bd[i];
var name=_2bf[0];
var _2c1=_2bf[1];
req.setRequestHeader(name,_2c1);
}
}
return self.sendXMLHttpRequest(req,opts.sendContent);
},_buildURL:function(url){
if(arguments.length>1){
var m=MochiKit.Base;
var qs=m.queryString.apply(null,m.extend(null,arguments,1));
if(qs){
return url+"?"+qs;
}
}
return url;
},doSimpleXMLHttpRequest:function(url){
var self=MochiKit.Async;
url=self._buildURL.apply(self,arguments);
return self.doXHR(url);
},loadJSONDoc:function(url){
var self=MochiKit.Async;
url=self._buildURL.apply(self,arguments);
var d=self.doXHR(url,{"mimeType":"text/plain","headers":[["Accept","application/json"]]});
d=d.addCallback(self.evalJSONRequest);
return d;
},wait:function(_2ca,_2cb){
var d=new MochiKit.Async.Deferred();
var m=MochiKit.Base;
if(typeof (_2cb)!="undefined"){
d.addCallback(function(){
return _2cb;
});
}
var _2ce=setTimeout(m.bind("callback",d),Math.floor(_2ca*1000));
d.canceller=function(){
try{
clearTimeout(_2ce);
}
catch(e){
}
};
return d;
},callLater:function(_2cf,func){
var m=MochiKit.Base;
var _2d2=m.partial.apply(m,m.extend(null,arguments,1));
return MochiKit.Async.wait(_2cf).addCallback(function(res){
return _2d2();
});
}});
MochiKit.Async.DeferredLock=function(){
this.waiting=[];
this.locked=false;
this.id=this._nextId();
};
MochiKit.Async.DeferredLock.prototype={__class__:MochiKit.Async.DeferredLock,acquire:function(){
var d=new MochiKit.Async.Deferred();
if(this.locked){
this.waiting.push(d);
}else{
this.locked=true;
d.callback(this);
}
return d;
},release:function(){
if(!this.locked){
throw TypeError("Tried to release an unlocked DeferredLock");
}
this.locked=false;
if(this.waiting.length>0){
this.locked=true;
this.waiting.shift().callback(this);
}
},_nextId:MochiKit.Base.counter(),repr:function(){
var _2d5;
if(this.locked){
_2d5="locked, "+this.waiting.length+" waiting";
}else{
_2d5="unlocked";
}
return "DeferredLock("+this.id+", "+_2d5+")";
},toString:MochiKit.Base.forwardCall("repr")};
MochiKit.Async.DeferredList=function(list,_2d7,_2d8,_2d9,_2da){
MochiKit.Async.Deferred.apply(this,[_2da]);
this.list=list;
var _2db=[];
this.resultList=_2db;
this.finishedCount=0;
this.fireOnOneCallback=_2d7;
this.fireOnOneErrback=_2d8;
this.consumeErrors=_2d9;
var cb=MochiKit.Base.bind(this._cbDeferred,this);
for(var i=0;i<list.length;i++){
var d=list[i];
_2db.push(undefined);
d.addCallback(cb,i,true);
d.addErrback(cb,i,false);
}
if(list.length===0&&!_2d7){
this.callback(this.resultList);
}
};
MochiKit.Async.DeferredList.prototype=new MochiKit.Async.Deferred();
MochiKit.Async.DeferredList.prototype._cbDeferred=function(_2df,_2e0,_2e1){
this.resultList[_2df]=[_2e0,_2e1];
this.finishedCount+=1;
if(this.fired==-1){
if(_2e0&&this.fireOnOneCallback){
this.callback([_2df,_2e1]);
}else{
if(!_2e0&&this.fireOnOneErrback){
this.errback(_2e1);
}else{
if(this.finishedCount==this.list.length){
this.callback(this.resultList);
}
}
}
}
if(!_2e0&&this.consumeErrors){
_2e1=null;
}
return _2e1;
};
MochiKit.Async.gatherResults=function(_2e2){
var d=new MochiKit.Async.DeferredList(_2e2,false,true,false);
d.addCallback(function(_2e4){
var ret=[];
for(var i=0;i<_2e4.length;i++){
ret.push(_2e4[i][1]);
}
return ret;
});
return d;
};
MochiKit.Async.maybeDeferred=function(func){
var self=MochiKit.Async;
var _2e9;
try{
var r=func.apply(null,MochiKit.Base.extend([],arguments,1));
if(r instanceof self.Deferred){
_2e9=r;
}else{
if(r instanceof Error){
_2e9=self.fail(r);
}else{
_2e9=self.succeed(r);
}
}
}
catch(e){
_2e9=self.fail(e);
}
return _2e9;
};
MochiKit.Async.EXPORT=["AlreadyCalledError","CancelledError","BrowserComplianceError","GenericError","XMLHttpRequestError","Deferred","succeed","fail","getXMLHttpRequest","doSimpleXMLHttpRequest","loadJSONDoc","wait","callLater","sendXMLHttpRequest","DeferredLock","DeferredList","gatherResults","maybeDeferred","doXHR"];
MochiKit.Async.EXPORT_OK=["evalJSONRequest"];
MochiKit.Async.__new__=function(){
var m=MochiKit.Base;
var ne=m.partial(m._newNamedError,this);
ne("AlreadyCalledError",function(_2ed){
this.deferred=_2ed;
});
ne("CancelledError",function(_2ee){
this.deferred=_2ee;
});
ne("BrowserComplianceError",function(msg){
this.message=msg;
});
ne("GenericError",function(msg){
this.message=msg;
});
ne("XMLHttpRequestError",function(req,msg){
this.req=req;
this.message=msg;
try{
this.number=req.status;
}
catch(e){
}
});
this.EXPORT_TAGS={":common":this.EXPORT,":all":m.concat(this.EXPORT,this.EXPORT_OK)};
m.nameFunctions(this);
};
MochiKit.Async.__new__();
MochiKit.Base._exportSymbols(this,MochiKit.Async);
if(typeof (MochiKit)=="undefined"){
MochiKit={};
}
if(typeof (MochiKit.MochiKit)=="undefined"){
MochiKit.MochiKit={};
}
MochiKit.MochiKit.NAME="MochiKit.MochiKit";
MochiKit.MochiKit.VERSION="1.4.2";
MochiKit.MochiKit.__repr__=function(){
return "["+this.NAME+" "+this.VERSION+"]";
};
MochiKit.MochiKit.toString=function(){
return this.__repr__();
};
MochiKit.MochiKit.SUBMODULES=["Base",/*"Iter","Logging","DateTime","Format",*/"Async"/*,"DOM","Selector","Style","LoggingPane","Color","Signal","Position","Visual","DragAndDrop","Sortable"*/];
if(typeof (JSAN)!="undefined"||typeof (dojo)!="undefined"){
if(typeof (dojo)!="undefined"){
dojo.provide("MochiKit.MochiKit");
(function(lst){
for(var i=0;i<lst.length;i++){
dojo.require("MochiKit."+lst[i]);
}
})(MochiKit.MochiKit.SUBMODULES);
}
if(typeof (JSAN)!="undefined"){
(function(lst){
for(var i=0;i<lst.length;i++){
JSAN.use("MochiKit."+lst[i],[]);
}
})(MochiKit.MochiKit.SUBMODULES);
}
(function(){
var _823=MochiKit.Base.extend;
var self=MochiKit.MochiKit;
var _825=self.SUBMODULES;
var _826=[];
var _827=[];
var _828={};
var i,k,m,all;
for(i=0;i<_825.length;i++){
m=MochiKit[_825[i]];
_823(_826,m.EXPORT);
_823(_827,m.EXPORT_OK);
for(k in m.EXPORT_TAGS){
_828[k]=_823(_828[k],m.EXPORT_TAGS[k]);
}
all=m.EXPORT_TAGS[":all"];
if(!all){
all=_823(null,m.EXPORT,m.EXPORT_OK);
}
var j;
for(j=0;j<all.length;j++){
k=all[j];
self[k]=m[k];
}
}
self.EXPORT=_826;
self.EXPORT_OK=_827;
self.EXPORT_TAGS=_828;
}());
}else{
if(typeof (MochiKit.__compat__)=="undefined"){
MochiKit.__compat__=true;
}
(function(){
if(typeof (document)=="undefined"){
return;
}
var _82e=document.getElementsByTagName("script");
var _82f="http://www.w3.org/1999/xhtml";
var _830="http://www.w3.org/2000/svg";
var _831="http://www.w3.org/1999/xlink";
var _832="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul";
var base=null;
var _834=null;
var _835={};
var i;
var src;
for(i=0;i<_82e.length;i++){
src=null;
switch(_82e[i].namespaceURI){
case _830:
src=_82e[i].getAttributeNS(_831,"href");
break;
default:
src=_82e[i].getAttribute("src");
break;
}
if(!src){
continue;
}
_835[src]=true;
if(src.match(/MochiKit.js(\?.*)?$/)){
base=src.substring(0,src.lastIndexOf("MochiKit.js"));
_834=_82e[i];
}
}
if(base===null){
return;
}
var _838=MochiKit.MochiKit.SUBMODULES;
for(var i=0;i<_838.length;i++){
if(MochiKit[_838[i]]){
continue;
}
var uri=base+_838[i]+".js";
if(uri in _835){
continue;
}
if(_834.namespaceURI==_830||_834.namespaceURI==_832){
var s=document.createElementNS(_834.namespaceURI,"script");
s.setAttribute("id","MochiKit_"+base+_838[i]);
if(_834.namespaceURI==_830){
s.setAttributeNS(_831,"href",uri);
}else{
s.setAttribute("src",uri);
}
s.setAttribute("type","application/x-javascript");
_834.parentNode.appendChild(s);
}else{
document.write("<"+_834.nodeName+" src=\""+uri+"\" type=\"text/javascript\"></script>");
}
}
})();
}



