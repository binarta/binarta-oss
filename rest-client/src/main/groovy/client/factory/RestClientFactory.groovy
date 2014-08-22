package client.factory

import client.GroovyxRestClient
import client.RestClient
import groovyx.net.http.RESTClient

class RestClientFactory {
    static RestClient clientFor(String baseUrl) {
        new GroovyxRestClient(new RESTClient(baseUrl))
    }

    static RestClient clientFor(String baseUrl, String username, String password) {
        def client = clientFor(baseUrl)
        client.setBasicCredentials(username, password)
        return client
    }
}
