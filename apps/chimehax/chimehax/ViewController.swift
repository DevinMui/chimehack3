//
//  ViewController.swift
//  chimehax
//
//  Created by Jesse Liang on 8/27/16.
//  Copyright © 2016 Jesse Liang. All rights reserved.
//

import UIKit
import GoogleMaps
import Alamofire

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    var latG = Double()
    var longG = Double()
    
    var locationManager = CLLocationManager()
    
    // You don't need to modify the default init(nibName:bundle:) method.
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        // Request permission to use location service
        locationManager.requestWhenInUseAuthorization()
        
        Alamofire.request(.GET, "http://dhsrobotics.com:3000/gps").responseJSON {response in
            
            if let JSON = response.result.value {
                
                if (JSON.count == 0) {
                    
                } else {
                    
                    print(JSON)
                    let long = JSON["long"] as! String
                    let lat = JSON["lat"] as! String
                    
                    let latd = Double(lat)
                    let longd = Double(long)
                    
                    self.latG = latd!
                    self.longG = longd!
                    print (self.longG)
                    print(longd)
                    
                    self.loadView()
                }
                
            }
        }

    }
    
    
    override func loadView() {
        
        
        // Create a GMSCameraPosition that tells the map to display the
        // coordinate -33.86,151.20 at zoom level 6.
        let camera = GMSCameraPosition.cameraWithLatitude(37.4849824, longitude:-122.1501238, zoom: 15.0)
        let mapView = GMSMapView.mapWithFrame(CGRect.zero, camera: camera)
        mapView.myLocationEnabled = true
        self.view = mapView
        
        
        // Creates a marker in the center of the map.
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: latG, longitude: longG)
        marker.title = "Someone Needs Help!"
        marker.map = mapView
    }
}