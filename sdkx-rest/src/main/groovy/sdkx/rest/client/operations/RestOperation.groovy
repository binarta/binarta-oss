package sdkx.rest.client.operations

import sdk.client.BinartaClient

class RestOperation {
    def restClient, defaultArgs

    static responseHandlers = [
        '401': { throw new BinartaClient.Unauthorized() },
        '403': { throw new BinartaClient.Forbidden() },
        '404': {throw new BinartaClient.NotFound()},
        '500': { throw new BinartaClient.Error(it) },
        '200': {it}
    ]

    def execute(input) {
        def response = restClient."$input.method"(*: defaultArgs, *: input.args)
        responseHandlerFor(response.status)(response.data)
    }

    def responseHandlerFor(status) {
        responseHandlers."$status" ?: {}
    }
}
