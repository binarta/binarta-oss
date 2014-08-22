package sdkx.inmemory.client

import sdk.client.BinartaClient
import spock.lang.Specification

abstract class ClientSpec extends Specification {
    def namespace = 'N'

    abstract BinartaClient getClient()
}
