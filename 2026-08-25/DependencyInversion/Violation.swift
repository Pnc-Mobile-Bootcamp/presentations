class LoginViewModel {
    private let networkService = NetworkService()			// concrete, hardcoded
    private let analyticsService = FirebaseAnalytics()		// concrete, hardcoded

    func login(email: String, password: String) async {
            let result = await networkService.post("/auth/login", 
                    body: ["email": email, "password": password])
            analyticsService.track(event: "loggin_attempted")
            . . .
    }
}


func test() {

    let viewModel = LoginViewModel()
    // it will use the real NetworkService and FirebaseAnalyticsService
    // and we don't know that!!

}