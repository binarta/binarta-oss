package sdkx.rest.client.operations

import static groovyx.net.http.ContentType.HTML

class GetRequiredPermissionOperation {
    @Delegate RestOperation delegate = new RestOperation()

    def execute(args) {
        def data = delegate.execute(method:'get', args: [
            path: "config/requires.permission.$args.entity.$args.action",
            requestContentType: HTML
        ])
        return data.value.toString()
    }
}
