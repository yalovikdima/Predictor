//
//  ViewController.swift
//  iosApp
//
//  Created by Eugene Filipkov on 10.03.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import Firebase

@UIApplicationMain
public class AppDelegate: UIResponder, UIApplicationDelegate {
	public var window: UIWindow?

	public func application(_ application: UIApplication,
													didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
		FirebaseApp.configure()

		let window = UIWindow(frame: UIScreen.main.bounds)
		window.backgroundColor = .white

    let controller = ViewController()

    let navigationController = UINavigationController(rootViewController: controller)

		window.rootViewController = navigationController
		window.makeKeyAndVisible()
		self.window = window

		return true
	}
}
