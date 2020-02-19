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

### 数据库
