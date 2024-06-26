### Orm
#### Orm核心功能
- 数据持久化
- 简单查询语句支持
- 映射Entity类创建对应的集合（表）
- 数据缓存（游戏默认需要/web一般根据设置来是否开启）


#### 功能拆分
- 数据持久化（Persister）
- 查询语句支持（Query）
- 映射Entity类（通过mongodb driver支持实现）
- 数据缓存（Cache）


##### 