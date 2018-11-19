package ${packagePath}.model.vo;

import com.cm.common.basedao.Query;

/**
 * ${commentInfo}QueryVo类
 * @author ${author}
 * @date ${.now?date}
 */
public class ${entityName}QueryVo extends Query {
	
	private String id;
	
	private String ids;
	
	public void setId(String id){
		this.id=id;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setIds(String ids){
		this.ids=ids;
	}	
	
	public String getIds(){
		return this.ids;
	}
}