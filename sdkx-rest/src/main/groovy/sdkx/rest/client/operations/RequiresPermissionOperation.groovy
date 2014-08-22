package sdkx.rest.client.operations

class RequiresPermissionOperation {
    @Delegate RestOperation delegate = new RestOperation()

    def execute(args) {
        delegate.execute(method: 'post', args: [
            path: '/config',
            body: [
                id   : "requires.permission.${args.entity}.${args.action}".toString(),
                value: args.permission
            ]
        ])
    }
}
