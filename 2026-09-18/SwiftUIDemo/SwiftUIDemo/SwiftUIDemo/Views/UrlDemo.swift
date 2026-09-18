//
//  UrlDemo.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//
import SwiftUI

struct UrlDemo: View {
    
    @State private var fact: String = "Tap the button for a cat fact!"
    
    var body: some View {
        VStack {
            Text("Cat Fact")
                .font(.largeTitle)
                .padding()
            
            Text(fact)
                .font(.title)
                .padding()
            Spacer()
            Button("Get Fact") {
                Task {
                    let url = URL(string: "https://catfact.ninja/fact")!
                    
                    do {
                        let (data, response) = try await URLSession.shared.data(from: url)
                        
                        // response is a URLResponse
                        // we need it as a HTTPURLResponse
                        if let httpResponse = response as? HTTPURLResponse,
                           httpResponse.statusCode == 200 {
                            // data is the binary body of the response
                            // we want it converted to a string
                            if let jsonString = String(data: data, encoding: .utf8) {
                                fact = jsonString
                            }
                        }
                    }
                    catch {
                        
                    }
                }
            }
            .buttonStyle(.borderedProminent)
        }
    }
}

#Preview {
    UrlDemo()
}
