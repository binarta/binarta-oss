package sdkx.inmemory.client

abstract class ConfigureGatewaySpec extends ClientSpec {
    def input = [entity:'E', args:[arg:'A']]

    def "configure a gateway"() {
        when:
        client.configureGateway(input)

        then:
        gatewayWasConfigured(input)
    }

    abstract void gatewayWasConfigured(args)
}
