package sdkx.rest.client.config

baseUrl = 'http://api.binarta.dev.thinkerit.be/'
environments {
    demo {
        baseUrl = 'http://api.binarta.demo.thinkerit.be/'
    }
    prod {
        baseUrl = 'https://api.binarta.com/'
    }
}
