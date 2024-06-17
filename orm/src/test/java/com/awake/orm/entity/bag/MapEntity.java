package com.awake.orm.entity.bag;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.anno.Persister;
import com.awake.orm.model.IEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: MapEntity
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 11:42
 **/

@EntityCache(persister = @Persister("time30s"))
@Data
public class MapEntity implements IEntity<Long> {
    @Id
    private long id;

    private Map<String, ItemBag> bagMap = new HashMap<>();

    private Map<String, Map<String, String>> baseMap = new HashMap<>();

    private Map<Long, String> longStringMap = new HashMap<>();
    private Map<Integer, String> intStringMap = new HashMap<>();
    private Map<Integer, Map<Integer, String>> intBaseMap = new HashMap<>();
    private Map<Character, ItemBag> charBagMap = new HashMap<>();
    private Map<Boolean, ItemBag> boolBagMap = new HashMap<>();
    private Map<Byte, ItemBag> byteBagMap = new HashMap<>();
    private Map<Short, ItemBag> shortBagMap = new HashMap<>();
    private Map<Integer, ItemBag> intBagMap = new HashMap<>();
    private Map<Long, ItemBag> longBagMap = new HashMap<>();
    private Map<Float, ItemBag> floatBagMap = new HashMap<>();
    private Map<Double, ItemBag> doubleBagMap = new HashMap<>();

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }
}
