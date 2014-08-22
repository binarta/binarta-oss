package client.factory

import client.GroovyxRestClient
import client.RestClient
import groovyx.net.http.AuthConfig
import groovyx.net.http.RESTClient
import spock.lang.Specification

class RestClientFactorySpec extends Specification {
    String authUser
    String authPass

    def setup() {
        AuthConfig.metaClass.static.basic = {String user, String pass ->
            authUser = user
            authPass = pass
        }
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass AuthConfig
    }

    def "factory constructs a RestClient implementation"() {
        when:
        def client = RestClientFactory.clientFor("base-url")

        then:
        client in RestClient
        client in GroovyxRestClient
        client.client in RESTClient
        client.client.uri.toString() == 'base-url'
    }

    def "factory constructs RestClient with given credentials"() {
        when:
        RestClientFactory.clientFor('url', 'username', 'password')

        then:
        authUser == 'username'
        authPass == 'password'
    }
}
