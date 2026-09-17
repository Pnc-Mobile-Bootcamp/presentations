//
//  LoginView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import SwiftUI

struct LoginView: View {
    
    @State private var viewModel = ViewModel()
    @EnvironmentObject var authStatus: AuthStatus
    
    
    var body: some View {
        VStack {
            Text("Login")
                .font(Font.largeTitle.bold())
            
            TextField("Username", text: $viewModel.credentials.username)
            SecureField("Password", text: $viewModel.credentials.password)
            
            if viewModel.isBusy {
                ProgressView()
            }
            
            Button("Log in") {
                Task {
                    let response = await viewModel.login()
                    if let resp = response {
                        authStatus.updateLoginStatus(success: resp.success,
                                                     authToken: resp.accessToken,
                                                     refreshToken: resp.refreshToken)
                    }
                }
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.loginDisabled)
            
            if !viewModel.errorMessage.isEmpty {
                Text(viewModel.errorMessage)
                    .foregroundColor(Color.red)
            }
            
            Spacer()
            
        }
        .padding(20)
        .autocapitalization(.none)
        .disabled(viewModel.isBusy)
    }
}

extension LoginView {
    
    @Observable
    class ViewModel {
        
        var credentials = LoginModel()
        var isBusy = false
        
        var loginDisabled: Bool {
            credentials.username.isEmpty || credentials.password.isEmpty
        }
        
        var errorMessage: String = ""
        
        func login() async -> LoginResponse? {
            isBusy = true
            
            do {
                let result = try await AuthService.shared.login(credentials: credentials)
                isBusy = false
                if !result.success {
                    errorMessage = "Invalid username/password combination"
                }
                return result
            }
            catch {
                errorMessage = "\(error)"
                
            }
            isBusy = false
            return nil
        }
        
        
    }
    
}

#Preview {
    LoginView()
}
