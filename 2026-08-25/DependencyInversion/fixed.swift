protocol NetworkServiceProtocol {
    func post(_ path: String, body: [String: Any]) async -> Result<data, Error>
}

protocol AnalyticsServiceProtocol {
    func track(event: String)
}

class LoginViewModel {
    private let networkService: NetworkServiceProtocol
    private let analyticsService: AnalyticsServiceProtocol

    init(networkService: NetworkServiceProtocol, analyticsService: AnalyticsServiceProtocol) {
        self.networkService = networkService
        self.analyticsService = analyticsService
    }

    func login(email: String, password: String) async {
        let result = await networkService.post("/auth/login", 
                body: ["email": email, "password": password])
        analyticsService.track(event: "loggin_attempted")
        . . .
    }
}


// in tests:
func test() {

    let viewModel = LoginViewModel(networkService: MockNetworkService(),
                                    analyticsService: MockAnalyticsService())
    await viewModel.login("test@example.com", "12345")

}



