class TimesheetManager {

	func fetchTimesheets(employeeId: String) -> [Timesheet] {
		let url = "https://api.internal.com/timesheets/(id)"  // hardcoded!
		return parseJSON(URLSession.shared.dataTask(url: url))
	}
	func approveTimesheet(_ t: Timesheet) { ... }

	func exportToCSV(_ timesheets: [Timesheet]) -> String { ... }

	func sendEmail(to: String, body: String) { ... }

	func writeToAuditLog(_ msg: String) { ... }
    
}
