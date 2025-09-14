# 🪄 RenamerAI Agent

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)

> ✨ Rename your screenshots intelligently with the power of AI.  
> Built with **Spring Boot + Spring AI + Picocli**, packaged as a CLI tool.

---

## 📖 Overview

RenamerAI is a **command-line tool** that automatically renames your screenshots using **AI-powered suggestions**.  
No more cryptic filenames — make your project, photo collection, or documents look **organized and meaningful** in seconds.

---

## 🚀 Features

- ✅ Rename images using AI assistance  
- ✅ Dry-run mode to preview changes before applying  
- ✅ Lightweight CLI — no web server needed  
- ✅ Built on **Spring Boot (non-web)** + **Picocli**  
- ✅ Easily extendable with new AI models

## 🚀 Upcoming Feature

- ✅ Image Classifier Tool.

---

## 🛠️ Installation

Clone the repository:

```bash
git clone https://github.com/<your-username>/renamerai.git
cd renamerai
mvn clean package
java -jar target/renamerai-0.0.1-SNAPSHOT.jar <path>
Usage: agent [-dhV] <path>
Renames images using AI assistance

      <path>      The path to the file or directory
  -d, --dry-run   Perform a dry run without renaming files
  -h, --help      Show this help message and exit
  -V, --version   Print version information and exit
ex: java -jar renamerai-0.0.1-SNAPSHOT.jar -- /home/mibatman/Desktop/Projects/renamerai
