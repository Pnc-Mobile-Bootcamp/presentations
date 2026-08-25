class TimesheetRepository {
    func fetchTimesheets(employeeId: String) -> [Timesheet] {
		let url = "https://api.internal.com/timesheets/(id)"  // hardcoded!
		return parseJSON(URLSession.shared.dataTask(url: url))
	}
    func updateStatus(_ timesheet: Timesheet, status: TimesheetStatus) { }
}

class EmailNotificationService: NotificationService {
    func sendNotification(to recipient: String, body: String) {
        SMTPClient.send(to: recipient, body: body)
    }
}

class AuditLogger {
    func log(_ message: String) {
        FileManager.default.append(message)
    }
}

class TimesheetApprovalCoordinator {

    init(repository: TimesheetRepository,
        notificationService: NotificationService,
        auditLogger: AuditLogger) {}
	
	func approveTimesheet(_ timesheet: Timesheet) {
        repository.updateStatus(timesheet, status: .approved)
        notificationService.sendNotification(to: timesheet.employee.email,
            body: "Your timesheet for \(timesheet.weekOf) has been approved.")
        auditLogger.log("Approved timesheet \(timesheet.id) at \(Date())")
    }

}

class TimesheetExporter {
    func exportToCSV(_ timesheets: [Timesheet]) -> String { ... }

}
