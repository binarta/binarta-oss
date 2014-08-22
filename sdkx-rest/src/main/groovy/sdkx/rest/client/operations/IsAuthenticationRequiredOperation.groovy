package sdkx.rest.client.operations

import static groovyx.net.http.ContentType.HTML

class IsAuthenticationRequiredOperation {
    @Delegate RestOperation operation = new RestOperation()

    def execute(args) {
        def data = operation.execute(method:'get', args: [
            path: "/config/requires.authentication.$args.entity.$args.action",
            requestContentType: HTML
        ])
        return data.value.toBoolean()
    }
}
