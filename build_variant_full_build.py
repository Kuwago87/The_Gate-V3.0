import os
import shutil
import subprocess
from pathlib import Path

def main():
    # Get script directory (equivalent to $PSScriptRoot)
    root = Path(__file__).resolve().parent

    print("\nBuild which variant?")
    print(" [1] Paper only")
    print(" [2] Spigot only")
    print(" [3] Both (default)")
    
    choice = input("Enter a number and press Enter: ").strip()

    if choice == "1":
        variants = ["paper"]
    elif choice == "2":
        variants = ["spigot"]
    else:
        variants = ["paper", "spigot"]

    print(f"\nBuilding: {', '.join(variants)}")
    
    results = {}

    for variant in variants:
        common_dir = root / "Common"
        variant_dir = root / variant
        build_dir = root / f"dist-{variant}"

        print(f"\n=== {variant} ===")

        if not common_dir.exists():
            print(f"[ERROR] Could not find common folder at {common_dir} - skipping {variant}.")
            results[variant] = "MISSING common/"
            continue

        if not variant_dir.exists():
            print(f"[ERROR] Could not find {variant} folder at {variant_dir} - skipping {variant}.")
            results[variant] = f"MISSING {variant}/"
            continue

        print(f"Assembling into {build_dir} ...")

        # Start clean every time
        if build_dir.exists():
            shutil.rmtree(build_dir)
        build_dir.mkdir(parents=True, exist_ok=True)

        # 1. Copy everything shared
        for item in common_dir.iterdir():
            dest = build_dir / item.name
            if item.is_dir():
                shutil.copytree(item, dest, dirs_exist_ok=True)
            else:
                shutil.copy2(item, dest)

        # 2. Copy the variant-specific files on top
        for item in variant_dir.iterdir():
            dest = build_dir / item.name
            if item.is_dir():
                shutil.copytree(item, dest, dirs_exist_ok=True)
            else:
                shutil.copy2(item, dest)

        # 3. Run mvn clean package
        print(f"Running 'mvn clean package' inside {build_dir}...")
        try:
            # shell=True allows Windows to find mvn if it is an .cmd or .bat file in PATH
            result = subprocess.run(
                ["mvn", "clean", "package"], 
                cwd=build_dir, 
                shell=os.name == 'nt'
            )
            
            if result.returncode == 0:
                print(f"[SUCCESS] Build for {variant} completed.")
                results[variant] = "SUCCESS"
            else:
                print(f"[ERROR] Maven build failed for {variant}.")
                results[variant] = "BUILD FAILED"
        except FileNotFoundError:
            print("[ERROR] 'mvn' command not found. Ensure Maven is installed and in your PATH.")
            results[variant] = "MAVEN NOT FOUND"

    print("\n" + "="*30)
    print("FINAL BUILD SUMMARY:")
    print("="*30)
    for variant, status in results.items():
        print(f"  {variant}: {status}")

if __name__ == "__main__":
    main()
