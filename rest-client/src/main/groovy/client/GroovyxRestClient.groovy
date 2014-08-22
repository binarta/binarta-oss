package client

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

class GroovyxRestClient implements RestClient {

    RESTClient client

    GroovyxRestClient(RESTClient client) {
        this.client = client
    }

    @Override
    RestClient.Response get(Map details) {
        sanitize {
            wrap(client.get(details))
        }
    }

    private RestClient.Response sanitize(Closure<RestClient.Response> c) {
        try {
            c()
        } catch (HttpResponseException e) {
            wrap(e.response)
        }
    }

    private RestClient.Response wrap(response) {
        new ResponseWrapper(delegate: response)
    }

    private class ResponseWrapper implements RestClient.Response {
        def delegate

        @Override
        int getStatus() {
            delegate.status
        }

        @Override
        String getContentType() {
            delegate.contentType
        }

        @Override
        Object getData() {
            delegate.data

        }
    }

    private RestClient.Response wrap(HttpResponseDecorator response) {
        new ResponseDecoratorWrapper(delegate: response)
    }

    private class ResponseDecoratorWrapper implements RestClient.Response {
        @Delegate HttpResponseDecorator delegate
    }

    @Override
    RestClient.Response post(Map details) {
        sanitize {
            wrap(client.post(details))
        }
    }

    @Override
    RestClient.Response put(Map details) {
        sanitize {
            wrap(client.put(details))
        }
    }

    @Override
    RestClient.Response delete(Map details) {
        sanitize {
            wrap(client.delete(details))
        }
    }

    @Override
    void setBasicCredentials(String username, String password) {
        client.auth.basic(username, password)
    }
}
