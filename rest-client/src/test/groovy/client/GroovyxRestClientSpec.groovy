package client

import groovyx.net.http.RESTClient
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler

import javax.servlet.http.HttpServletResponse

class GroovyxRestClientSpec extends RestClientSpec {
    Server server
    RequestHandler handler
    GroovyxRestClient client

    def setup() {
        handler = new RequestHandler()
        server = new Server(5080)
        server.handler = handler
        server.start()
    }

    private static class RequestHandler extends AbstractHandler {
        def path
        def method
        def body
        def contentType
        Map headers = [:]
        Map queryArgs = [:]
        private boolean requiresAuthentication

        @Override
        void handle(String target, Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
            new RequestResponseWrapper(target:target, request:baseRequest, response:response).handle()
        }

        private class RequestResponseWrapper {
            String target
            Request request
            HttpServletResponse response

            void handle() {
                if (shouldProvideCredentials()) requestCredentials()
                else handleRequest()
                end()
            }

            private boolean shouldProvideCredentials() {
                requiresAuthentication && !request.getHeader('Authorization')
            }

            private void requestCredentials() {
                response.setHeader('WWW-Authenticate', 'Basic realm="Secure Area"')
                response.status = javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
            }

            private void handleRequest() {
                path = target
                method = request.method
                body = request.inputStream.text
                contentType = request.contentType
                request.headerNames.each {headers[it] = request.getHeader(it)}
                queryArgs = request.parameterMap
                ok()
            }

            private void ok() {
                response.contentType = "text/html"
                response.status = javax.servlet.http.HttpServletResponse.SC_OK
                response.writer.println("Hello World")
            }

            private void end() {
                request.handled = true
            }
        }

        void requiresAuthentication() {
            requiresAuthentication = true
        }
    }

    def cleanup() {
        server.stop()
    }

    @Override
    void setBaseUrl(String url) {
        client = new GroovyxRestClient(new RESTClient(url))
    }

    @Override
    RestClient getClient() {
        client
    }

    @Override
    String getMethod() {
        handler.method
    }

    @Override
    String getPath() {
        handler.path
    }

    @Override
    String getBody() {
        handler.body
    }

    @Override
    String getContentType() {
        handler.contentType
    }

    @Override
    void requiresAuthentication() {
        handler.requiresAuthentication()
    }

    @Override
    String getHeader(String name) {
        handler.headers[name]
    }

    @Override
    String getQueryArg(String name) {
        handler.queryArgs[name][0]
    }
}
