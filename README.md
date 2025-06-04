# System Benchmarking Project

This project is a Java-based benchmarking suite designed to measure the performance of various system components, including CPU, RAM, and HDD. It provides a set of benchmarks that can be run from the command line to evaluate your system's capabilities.

Built for the Digital Computers laboratory in 2nd year at Politehnica University of Timisoara.

## Team

-   Radu Mirzoca (@RaduTek)
-   Iasmina Stoicovici (@sqrt2x4)
-   Flavius Neacsu (@Snowman23)
-   Cristiana Ciapsa (@Criuwu)
-   Bianca Marincu (@tanti-bia)

## Requirements

-   Provided JAR file targets Java 24
    -   Check releases tab for built file
-   Sufficient permissions to create and write files for HDD and virtual memory benchmarks

## Features

-   **CPU Floating Point Benchmark**: Measures CPU performance with floating-point operations.
-   **HDD Write Speed Benchmark**: Tests sequential and random write speeds of your hard drive.
-   **Recursive Prime Benchmark**: Calculates prime numbers recursively to stress the CPU.
-   **Virtual Memory Benchmark**: Measures RAM and virtual memory access speeds by mapping large files into memory.
-   **Demo Benchmark**: Demonstrates the benchmarking framework and logging.

## System Information

Before running any benchmark, the suite prints detailed system information, including OS, architecture, processor count, and available physical memory.

## Usage

Compile the project and run the main class:

```sh
java -jar UPTBenchmark.jar <benchmark> [args...]
```

### Available Benchmarks

#### 1. Demo Benchmark

**Command:**

```
demo
```

Runs a demonstration of the benchmarking framework.

#### 2. CPU Floating Point Benchmark

**Command:**

```
cpufloat
```

Measures CPU floating-point performance.

#### 3. HDD Write Speed Benchmark

**Command:**

```
hddwrite
```

Performs sequential and fixed-buffer write speed tests on your HDD.

#### 4. HDD Random Write Benchmark

**Command:**

```
hddrandom
```

Performs random write speed tests on your HDD.

#### 5. Recursive Prime Benchmark

**Command:**

```
recursiveprime [unrollFactor]
```

-   `unrollFactor` (optional): Integer to control loop unrolling for the prime calculation.

#### 6. Virtual Memory Benchmark

**Command:**

```
virtualmem [fileSizeMB] [bufferSizeBytes]
```

-   `fileSizeMB` (optional): Size of the file to map into memory, in megabytes (default: 2048 MB).
-   `bufferSizeBytes` (optional): Size of the buffer for read/write operations, in bytes (default: 4096).

**Example:**

```
java -jar UPTBenchmark.jar testbench.Testbench virtualmem 4096 8192
```

Runs the virtual memory benchmark with a 4 GB file and 8 KB buffer.

## Output

Each benchmark prints its results to the console, including performance metrics such as MB/s, elapsed time, or calculated values.
