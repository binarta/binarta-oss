package sdkx.inmemory.client

abstract class IsPermissionRequiredSpec extends ClientSpec {
    def "look up required permission"() {
        given:
        interaction {
            permissionRequirementWasRegistered(entity:'E', action:'A', permission:'P')
        }

        when:
        def permission = client.getRequiredPermission(entity:'E', action:'A')

        then:
        permission == 'P'
    }

    abstract permissionRequirementWasRegistered(args)
}
