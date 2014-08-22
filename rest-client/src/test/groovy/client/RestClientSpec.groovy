package client

import org.apache.commons.codec.binary.Base64
import spock.lang.Specification

abstract class RestClientSpec extends Specification {
    def response

    def "rest client can perform requests using different methods"() {
        given:
        baseUrl = 'http://localhost:5080/'

        when:
        response = client."${action.toLowerCase()}"(path: '/endpoint')

        then:
        method == action
        path == '/endpoint'
        response.status == 200
        response.contentType == 'text/html'
        response.data == 'Hello World\n'

        where:
        action << ['GET', 'POST', 'PUT', 'DELETE']
    }

    abstract void setBaseUrl(String url)

    abstract RestClient getClient()

    abstract String getMethod()

    abstract String getPath()

    def "a body can be passed along with a request"() {
        given:
        baseUrl = 'http://localhost:5080/'

        when:
        response = client."${action.toLowerCase()}"(path: '/endpoint', body: 'body', requestContentType: 'text/plain')

        then:
        body == 'body'
        contentType == 'text/plain'

        where:
        action << ['POST', 'PUT']
    }

    abstract String getBody()

    abstract String getContentType()

    def "handling unauthorized response"() {
        given:
        baseUrl = 'http://localhost:5080/'
        requiresAuthentication()

        when:
        response = client."${action.toLowerCase()}"(path: 'irrelevant-path')

        then:
        response.status == 401

        where:
        action << ['GET', 'POST', 'PUT', 'DELETE']
    }

    abstract void requiresAuthentication()

    def "supports basic authentication"() {
        given:
        baseUrl = 'http://localhost:5080/'
        requiresAuthentication()

        when:
        client.setBasicCredentials("username", "password")
        client."${action.toLowerCase()}"(path: 'irrelevant-path')

        then:
        def creds = new BasicCredentials(getHeader('Authorization'))
        creds.username == 'username'
        creds.password == 'password'

        where:
        action << ['GET', 'POST', 'PUT', 'DELETE']
    }

    abstract String getHeader(String name)

    private static class BasicCredentials {
        String username
        String password

        BasicCredentials(String header) {
            def args = new String(new Base64().decode((header - 'Basic').bytes)).split(':')
            username = args[0]
            password = args[1]
        }
    }

    def "headers can be passed to the request"() {
        given:
        baseUrl = 'http://localhost:5080/'
        def headers = [header1:'value1', header2:'value2']

        when:
        client."${method.toLowerCase()}"(path:'irrelevant-path', headers:headers)

        then:
        getHeader('header1') == 'value1'
        getHeader('header2') == 'value2'

        where:
        method << ['GET', 'PUT', 'POST', 'DELETE']
    }

    def "query arguments can be passed to the request"() {
        given:
        baseUrl = 'http://localhost:5080/'
        def args = [arg1:'value1', arg2:'value2']

        when:
        client.get(path:'irrelevant-path', query:args)

        then:
        getQueryArg('arg1') == 'value1'
        getQueryArg('arg2') == 'value2'
    }

    abstract String getQueryArg(String name)
}
