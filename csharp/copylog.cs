using System;
using System.Collections.Generic;
using System.IO;
//using System.Text.Json;       // .net fw at 4.6.1, not available (?)
using Newtonsoft.Json.Linq;

namespace copylog
{

    /**
     * There's a daily need to read logs from network/share folders, each time having to navigate
     * to the folder, wait for it to update (not synced frequently), open the file and read it there (which refreshes).
     * Another option is to copy it locally, which still requires the time to navigate and copy, which is slow through
     * Windows UI services.
     * 
     * Solution: create a command that will copy the file using only a couple tags/switches, and to do that quickly.
     * This program seems to do that and it does it in about a second for relatively large files.
     * To make this happen, the program needs the info on the individual log locations/names.
     * 
     * TODO: move the server/log information to a json file that can be read in and updated independently.
     * 
     * 20211130:anf
     */
    class Program
    {
        static void print(object o) { Console.WriteLine($"{o}"); }

        static void Main(string[] args)
        {
            //args = new string[] {"v10","pc","prod"};

            if (args.Length < 3)
            {
                Console.WriteLine("Requires 3 arguments");
                Console.WriteLine("Usage: 'copylogs {segment} {app} {env}' => 'copylogs v10 pc dev'");
#if DEBUG
                Console.WriteLine("Press any key...");
                Console.ReadKey();
#endif
                Environment.Exit(-1);
            }

            var segment = args[0];
            var app = args[1];
            var env = args[2];           

            /*
             * Beginning with C# 3, variables that are declared at method scope can have an implicit "type" var. 
             * An implicitly typed local variable is strongly typed just as if you had declared the type yourself, 
             * but the compiler determines the type.
             * 
             * https://docs.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/var
             */

            var logmap = new LogMapJsonReader().ReadLogMapping();
            var start = DateTime.Now;
            var src = "";
            try
            {
                src = (string)logmap[segment][app][env];
                if(src == null || src.Equals(""))
                {
                    print("Unable to locate valid log info, aborting...");
                    Environment.Exit(-1);
                }
            }
            catch (NullReferenceException nre)
            {
                Console.WriteLine($"Unable to find log using keys [{segment}][{app}][{env}]");
                Console.WriteLine($"Aborting... {nre.GetType()}");
#if DEBUG
                Console.WriteLine("Press any key...");
                Console.ReadKey();
#endif
                Environment.Exit(-1);
            }

            Console.WriteLine($"Copying from {src}");
            string[] tokens = src.Split('/');
            string logfile = tokens[tokens.Length - 1];
            string destDir = "C:/tmp";

            var len = new FileInfo(Path.Combine(destDir, logfile)).Length;
            print($"Log file size: {len} bytes");

            if(File.Exists(Path.Combine(destDir, logfile)))
                File.Copy(src, Path.Combine(destDir, logfile), true);
            else
                File.Copy(src, Path.Combine(destDir, logfile));

            var stop = DateTime.Now;
            Console.WriteLine($"Process Completed in {(stop - start)} ");

#if DEBUG
            Console.WriteLine("Press any key...");
            Console.ReadKey();
#endif

        }
    }

    class LogMapJsonReader
    {
        /**
         * Read the log-location mappings from an external json file.
         * 
         * TODO: figure out whether the file reference is released and nothing's open
         */
        public JObject ReadLogMapping()
        {
            var path = "/af/git/toolstore/tools.windows/bin/logMap.json";
            Console.WriteLine($"Reading log location map from {path}");
            Console.WriteLine($"Path found: {File.Exists(path)}");            
            var jsonStr = File.ReadAllText(path);
            return JObject.Parse(jsonStr);
        }        
    }

    /** 
     * Transfer object/abstraction.  
     * 
     * Originally intended to use Dictionaries but opted to use 
     * JObject becuase it's accessed the same way and I don't 
     * have to write more code (move on to other things).
     * 
     * For some reason, a JObject will not cast to a LogMap.  
     * For this reason, program is using the JObject directly to access locations.  
     * This is good, why waste time on more code for a single-run utility.
     */
    class LogMap : JObject { }
}

