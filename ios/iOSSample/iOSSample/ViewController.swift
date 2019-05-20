//
//  ViewController.swift
//  iOSSample
//
//  Created by Dmitriy on 10/04/2019.
//  Copyright © 2019 MobileUp. All rights reserved.
//

import UIKit
import main

class ViewController: UIViewController {

    var job: Kotlinx_coroutines_coreJob? = nil
    @IBOutlet weak var label: UILabel!

    @IBAction func buttoClicked(_ sender: UIButton) {
        
        job?.cancel()
        job = CommonKt.startCountdown { i in
            if (i.intValue > 0) {
                self.label?.text =  i.stringValue
            } else {
                self.label?.text = CommonKt.createApplicationScreenMessage()
            }
            return KotlinUnit()
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        job?.cancel()
        job = nil
    }
}

