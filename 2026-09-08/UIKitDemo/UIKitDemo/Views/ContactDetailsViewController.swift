//
//  ContactDetailsViewController.swift
//  UIKitDemo
//
//  Created by user286430 on 9/8/26.
//


import UIKit

class ContactDetailsViewController: UIViewController {

    private let theLabel = UILabel()
    private let button = UIButton()
    private let contactContainer = UIStackView()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setup()
    }

    private func buttonTapped() {
        self.navigationController?.popViewController(animated: true)
    }

}

private extension ContactDetailsViewController {
    
    
    
    func setup() {
        self.view.backgroundColor = .white
        
        // configure our labels
        theLabel.text = "These are the contact details"
        theLabel.font = .systemFont(ofSize: 24, weight: .bold)
        
        
        // configure our button
        button.configuration = .filled()
        button.configuration?.title = "Close"
        button.addAction(UIAction { [weak self] _ in
            self?.buttonTapped()
        }, for: .touchUpInside)
        
        // add the UIViews to the container
        contactContainer.addArrangedSubview(theLabel)
        contactContainer.addArrangedSubview(button)
        
        // configure the container element
        contactContainer.backgroundColor = .gray.withAlphaComponent(0.1)
        contactContainer.axis = .vertical
        contactContainer.spacing = 8
        contactContainer.alignment = .leading
        
        // add padding
        contactContainer.isLayoutMarginsRelativeArrangement = true
        contactContainer.layoutMargins = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
        
        // rounded corners
        contactContainer.layer.cornerRadius = 8
        contactContainer.clipsToBounds = true
        
        
        // disable the auto-creation of constraints
        contactContainer.translatesAutoresizingMaskIntoConstraints = false
        
        self.view.addSubview(contactContainer)
        
        NSLayoutConstraint.activate([
            contactContainer.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 20),
            contactContainer.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            contactContainer.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
        ])
        
        
    }
    
}

