package sdkx.inmemory.client

class InMemoryConfigureGatewaySpec extends ConfigureGatewaySpec {
    InMemoryBinartaClient client = new InMemoryBinartaClient(namespace:namespace)

    void gatewayWasConfigured(args) {
        assert client.gatewayConfigurations[namespace][args.entity] == args.args
    }
}
