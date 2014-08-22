package sdkx.inmemory.client

import sdk.client.BinartaClient
import spock.lang.Specification

abstract class IsAuthRequiredSpec extends Specification {
    def namespace = 'N'

    def "check for auth requirement"() {
        given:
        interaction {
            authenticationWasRegistered(entity:'E', action:'A')
        }

        when:
        def result = client.isAuthenticationRequired(entity:'E', action:'A')

        then:
        result
    }
    abstract BinartaClient getClient()
    abstract authenticationWasRegistered(args)
}
