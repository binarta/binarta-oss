package sdkx.inmemory.client

import spock.lang.Specification

class InMemoryBinartaClientSpec extends Specification {
    def client = new InMemoryBinartaClient(namespace:'N')

    def "register authentication requirement"() {
        when:
        client.requiresAuthentication(entity:'E', action:'C')

        then:
        client.authenticationPredicates == [N:[[entity:'E', context:'C']]]
    }

    def "check for auth requirement"() {
        given:
        client.requiresAuthentication(entity:'E', action:'C')

        expect:
        client.isAuthenticationRequired(entity:'E', action:'C')
        !client.isAuthenticationRequired(entity:'E', action:'CC')
    }

    def "register permission requirement"() {
        when:
        client.requiresPermission(entity:'E', action:'C', permission:'P')

        then:
        client.permissionPredicates == [N:[[entity:'E', context:'C', permission:'P']]]
    }

    def "retrieve required permissions"() {
        given:
        client.requiresPermission(entity:'E', action:'C', permission:'P')

        expect:
        client.getRequiredPermissions(entity:'E', action:'C') == ['P']
    }

    def "registrations are namespace bound"() {
        given:
        client.requiresAuthentication(entity:'E', action:'A')
        client.namespace = 'NN'

        expect:
        !client.isAuthenticationRequired(entity:'E', action:'A')
    }
}
