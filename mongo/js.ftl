var conn=new Mongo('127.0.0.1:27018');
var db = conn.getDB("${dbname}");
<#if auth == 0>
db.auth('${username}','${password}');
</#if>
db.system.js.save({'_id':'area${citycode}',
                'value':function(query,carQuery){
                        if(!query){query={}};
                        query['statusInfo.point']={ $geoWithin :
                                    { $geometry :
                                   { type : "${type}",coordinates:${polygon}
                        }}};
                        var result ={};
                        result.code='${citycode}';
                        result.name='${cityname}';
                        result.parentCode='${parentCode}';
                        <#if citylon?? >result.lon=${citylon};</#if>
                        <#if citylat?? >result.lat=${citylat};</#if>
                        if(carQuery){
                        	var skip = carQuery.skip;
                        	var limit = carQuery.limit;
                        	var fields={};
                        	if(carQuery.simple==1){
                        		var fields = {'_id':1,'statusInfo.online':1,'statusInfo.point':1,'statusInfo.warn':1};
                        	}
                        	if(skip&&limit){
                        		result.carInfos=db.carInfo.find(query,fields).skip(skip).limit(limit).toArray();
                        	}else{
                        		result.carInfos=db.carInfo.find(query,fields).toArray();
                        		result.num=result.carInfos.length;
                        	}
                        }else{
                        	result.num=parseInt(db.carInfo.count(query));
                        }
                        return result;
}});