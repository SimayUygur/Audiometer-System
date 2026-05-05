# 🎧 Audiometer System (Multidisciplinary Project)

## 📌 Project Overview

This project is a multidisciplinary engineering study developed by students from Computer Engineering, Biomedical Engineering, Electrical & Electronics Engineering, and Software Engineering departments.

The aim is to design and simulate an **audiometer system** used to measure hearing thresholds across different frequencies and intensity levels.

---

## 💻 Computer Engineering Contribution

This repository contains the **Computer Engineering part** of the project, which includes:

* 🎛️ GUI development using Java (Swing)
* 🔌 Serial communication with hardware (jSerialComm)
* 📥 Receiving patient responses ("RESPONSE")
* 📊 Real-time audiogram visualization
* 🎯 Integration of system components

---

## ⚙️ Features

* Frequency selection (250 Hz – 8000 Hz)
* Intensity (dB) control
* Right / Left ear selection
* Real-time response handling
* Audiogram plotting:

  * 🔴 Right ear → O
  * 🔵 Left ear → X

---

## 🖥️ Technologies Used

* Java (Swing)
* jSerialComm (Serial Communication)
* JFreeChart (Visualization)

---

## 🔄 System Workflow

1. User selects frequency and intensity
2. Command is sent to the hardware system
3. Patient response is received ("RESPONSE")
4. Threshold is updated using the Hughson–Westlake method
5. Results are plotted on the audiogram

---

## 🚀 How to Run

1. Clone the repository
2. Add required libraries:

   * jSerialComm
   * JFreeChart
3. Run `Main.java`
4. Use GUI to simulate or test responses

---

## 📊 Sample Output

Audiogram graph showing hearing thresholds for both ears.

---

## 🤝 Contributors

* Computer Engineering: GUI & System Integration
* Biomedical Engineering: Algorithm & Medical Standards
* Electrical & Electronics Engineering: Signal Generation
* Software Engineering: Functional Implementation & Testing

---

## 📌 Notes

This project is developed for educational purposes as part of a multidisciplinary engineering course.

---

