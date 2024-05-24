package com.awake.orm.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.anno.Index;
import com.awake.orm.model.IEntity;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @version : 1.0
 * @ClassName: MailEntity
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 16:41
 **/
@EntityCache
@Data
public class MailEntity implements IEntity<String> {

    @Id
    private String id;

    @Index(ascending = true, unique = false)
    private String userName;

    private String content;

    //    @Index(ascending = true, unique = false, ttlExpireAfterSeconds = 10)
//    private Date createDate;
    /**
     * 存到Mongodb后发现该字段的值少了8个小时，即比实际保存时的时间晚了8小时。究其原因是因为Mongodb是以标准的格林尼治时间（GMT）为时间保存
     * 没有设置当地时区，而北京时间属于东八区，比其早了八小时
     */
    @Index(ascending = true, unique = false, ttlExpireAfterSeconds = 10)
    private Date createDate;

    public static MailEntity valueOf(String id, String userName, String content, Date createDate) {
        var entity = new MailEntity();
        entity.id = id;
        entity.userName = userName;
        entity.content = content;
        entity.createDate = createDate;
        return entity;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=  id;
    }


}
