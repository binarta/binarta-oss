package sdkx.inmemory.client

abstract class RequiresAuthenticationSpec extends ClientSpec {
    def namespace = 'N'

    def "register an authentication requirement"() {
        when:
        client.requiresAuthentication(entity:'E', action:'A')

        then:
        interaction {
            authenticationRequirementWasRegistered(namespace:namespace, entity:'E', action:'A')
        }
    }

    abstract authenticationRequirementWasRegistered(args)
}
