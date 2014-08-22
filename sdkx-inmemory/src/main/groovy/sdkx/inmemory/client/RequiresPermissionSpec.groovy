package sdkx.inmemory.client

abstract class RequiresPermissionSpec extends ClientSpec {
    def "permission requirement can be registered"() {
        when:
        client.requiresPermission(entity:'E', action:'A', permission:'P')

        then:
        interaction {
            permissionRequirementWasRegistered(entity:'E', action:'A', permission:'P')
        }
    }

    abstract permissionRequirementWasRegistered(args)
}
