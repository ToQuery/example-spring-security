### Spring Security Acl 的一些关键接口：

- Acl ： 每个领域对象有一个，并只有一个Acl对象，它的内部保存着AccessControlEntry (ace)，记住这是Acl的所有者。 一个Acl不直接引用领域对象，但是作为替代的是使用一个ObjectIdentity。 这个Acl保存在ACL_OBJECT_IDENTITY 表里。 

每一个领域对象都会对应一个Acl，而且只会对应一个Acl。
Acl是将Spring Security Acl中使用到的四个表串联起来的一个接口，其中会包含对象信息ObjectIdentity、对象的拥有者Sid和对象的访问控制信息AccessControlEntry。
在Spring Security Acl中直接与acl_object_identity表相关联的是Acl接口，因为acl_object_identity表中的数据是通过保存Acl来进行的。
一个Acl对应于一个ObjectIdentity，但是会包含有多个Sid和多个AccessControlEntry，即一个Acl表示所有Sid对一个ObjectIdentity的所有AccessControlEntry。
Acl的默认实现类是AclImpl，该类实现Acl接口、MutableAcl接口、AuditableAcl接口和OwnershipAcl接口

- AccessControlEntry ：一个 Acl 里有多个AccessControlEntry，在框架里常常略写成ACE。 每个ACE引用特别的Permission，Sid和Acl。 一个ACE可以授权或不授权(granting)，包含审核设置。 ACE保存在ACL_ENTRY 表里。

一个AccessControlEntry表示一条访问控制信息，一个Acl中可以拥有多个AccessControlEntry。
在Spring Security Acl中很多地方会使用ACE来简单的表示AccessControlEntry这个概念，比如insertAce其实表示的就是insert AccessControlEntry。
每一个AccessControlEntry表示对应的Sid对于对应的对象ObjectIdentity是否被授权某一项权限Permission，是否被授权将使用granting进行区分。AccessControlEntry对应表acl_entry。

- Permission ：一个 permission 表示特殊不变的位掩码，为位掩码和输出信息提供方便的功能。 上面的基本权限（位0到4）保存在BasePermission类里。

在Acl中使用一个bit掩码来表示一个Permission。
Spring Security的Acl中默认使用的是BasePermission，其中已经定义了0-4五个bit掩码，分别对应于1、2、4、8、16，代表五种不同的Permission，分别是read (bit 0)、write (bit 1)、create (bit 2)、delete (bit 3)和administer (bit 4)。
如果已经定义好的这五个bit掩码不能满足需求，我们可以对BasePermission进行扩展，也可以实现自己的Permission。Spring Security Acl默认的实现最多可以支持32个不同的掩码。

- Sid ：这个 ACL 模块需要引用主体(Princiapl)和GrantedAuthority[]。 间接的等级由Sid接口提供，简写成“安全标识”。 通常类包含PrincipalSid（表示主体在Authentication里）和GrantedAuthoritySid。 安全标识信息保存在ACL_SID 表里。

可以用来表示一个principal，或者是一个GrantedAuthority。其对应的实现类有表示principal的PrincipalSid和表示GrantedAuthority的GrantedAuthoritySid。其信息会保存在acl_sid表中。

- ObjectIdentity ：每个领域对象(ACL_CLASS)放在ACl模型的内部，使用ObjectIdentity。 默认实现叫做ObjectIdentityImpl 。

ObjectIdentity表示Spring Security Acl中一个域对象，其默认实现类是ObjectIdentityImpl。
ObjectIdentity并不是直接与acl_object_identity表相对应的，真正与acl_object_identity表直接相对应的是Acl。

- AclService 

AclService 是用来通过ObjectIdentity解析Acl的 ， 其默认实现类是 JdbcAclService ，重审操作代理 LookupStrategy 。 这个LookupStrategy为检索ACL信息提供高优化策略，使用批量检索（BasicLookupStrategy）然后支持自定义实现。

- MutableAclService

MutableAclService是用来对Acl进行持久化的，其默认实现类是JdbcMutableAclService。
JdbcMutableAclService是继承自JdbcAclService的，所以我们可以同时通过JdbcMutableAclService对Acl进行读取和保存。
如果我们希望自己来实现Acl信息的保存的话，我们也可以不使用该接口。

### 数据结构

- acl_class

| **字段名** | **类型** | **说明**           |
| ---------- | -------- | ------------------ |
| id         | number   | 主键               |
| class      | varchar  | 对象类型的全限定名 |

表acl_class是用来保存对象类型的，字段class中保存的是对应对象的全限定名。Acl需要使用它来区分不同的对象类型。

- acl_sid

| **字段名** | **类型** | **说明**        |
| ---------- | -------- | --------------- |
| id         | number   | 主键            |
| sid        | varchar  | 字符串类型的sid |
| principal  | boolean  | 是否用户        |

表acl_sid是用来保存Sid的。对于Acl而言，有两种类型的Sid一种是基于用户的Sid，叫PrincipalSid，另一种是基于GrantedAuthority的Sid，叫GrantedAuthoritySid。acl_sid表的sid字段存放的是用户名或者是GrantedAuthority的字符串表示。prinpal是用来区分对应的Sid是用户还是GrantedAuthority的。正如在前文所描述的那样，Acl中对象的权限是用来授予给Sid的，Sid有用户和GrantedAuthority之分，所以我们的对象权限是可以用来授予给用户或GrantedAuthority的。

- acl_object_identity

| **字段名**         | **类型** | **描述**                                                     |
| ------------------ | -------- | ------------------------------------------------------------ |
| id                 | number   | 主键                                                         |
| object_id_class    | number   | 关联acl_class，表示对象类型                                  |
| object_id_identity | number   | 对象的主键，对于相同的class而言，其需要是唯一的。对象的主键默认需要是Long型，或者可以转换为Long型的对象，如Integer、Short等。 |
| parent_object      | number   | 父对象的id，关联acl_object_identity                          |
| owner_sid          | number   | 拥有者的sid，关联acl_sid                                     |
| entries_inheriting | boolean  | 是否继承父对象的权限。打个比方，删除对象childObj需要有delete权限，用户A他没有childObj的delete权限，但是他有childObj的父对象parentObj的delete权限，当entries_inheriting为true时，用户A同样可以删除childObj。 |

表acl_object_identity是用来存放需要进行访问控制的对象的信息的。其保存的信息有对象的拥有者、对象的类型、对象的主键、对象的父对象和是否继承父对象的权限。

- acl_entry

| **字段名**          | **类型** | **说明**                                                  |
| ------------------- | -------- | --------------------------------------------------------- |
| id                  | number   | 主键                                                      |
| acl_object_identity | number   | 对应acl_object_identity的id                               |
| ace_order           | number   | 所属Acl的权限顺序                                         |
| sid                 | number   | 对应acl_sid的id                                           |
| mask                | number   | 权限对应的掩码                                            |
| granting            | boolean  | 是否授权                                                  |
| audit_success       | boolean  | 暂未发现其作用，Acl中有一个更新其值的方法，但未见被调用。 |
| audit_failure       | boolean  |                                                           |

表acl_entry是用于存放具体的权限信息的，从表结构我们也可以看出来，其描述的就是某个主体（Sid）对某个对象（acl_object_identity）是否（granting）拥有某种权限（mask）。当同一对象acl_object_identity在acl_entry表中拥有多条记录时，就会使用ace_order来标记对应的顺序，其对应于往Acl中插入AccessControlEntry时的位置，在进行权限判断时也是依靠ace_order的顺序来进行的，ace_order越小的越先进行判断。ace是Access Control Entry的简称。

> - https://www.baeldung.com/spring-security-acl
