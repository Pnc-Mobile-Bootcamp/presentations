//
//  ContactsViewController.swift
//  UIKitDemo
//
//  Created by user286430 on 9/8/26.
//

import UIKit

class ContactsViewController: UIViewController {

    private var employees: [Employee] = []
    let tableView = UITableView()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setup()
        loadData()
    }

    func loadData() {
        employees = [
            Employee(id: 101, firstName: "Antonio", lastName: "Banderas"),
            Employee(id: 102, firstName: "Gloria", lastName: "Estefan"),
            Employee(id: 103, firstName: "Tony", lastName: "Orlando"),
            Employee(id: 104, firstName: "Gladys", lastName: "Knight"),
        ]
    }
}

extension ContactsViewController: UITableViewDataSource, UITableViewDelegate {
    
    // MARK: - UITableViewDataSource methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // return the total number of rows we will have
        return employees.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // get a cell from the UITableView
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        
        // set the cell's contents
        cell.textLabel?.text = employees[indexPath.row].fullName
        
        return cell
    }
    
    // MARK: - UITableViewDelegate methods
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedEmployee = employees[indexPath.row]
        let detailsVC = ContactDetailsViewController()
        detailsVC.employee = selectedEmployee
        self.navigationController?.pushViewController(detailsVC, animated: true)
    }
    
    
    // MARK: - UI element configuration
    
    func setup() {
        self.view.backgroundColor = .white
        
        
        self.view.addSubview(tableView)
        
        // disable the auto-creation of constraints
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 20),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            tableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: 20)
        ])
        tableView.dataSource = self
        tableView.delegate = self
        
    }
    
}

