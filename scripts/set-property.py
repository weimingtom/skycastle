#! /usr/bin/python
# Sets the property in a java property file to the specified value, or adds it if it isn't present.
# Programmed 2008-11 by Hans Haggstrom (zzorn at iki.fi)
# Public domain, adapt and use as you want.

# NOTE: This is a huge amount of broilerplate, isn't there any better command line reader utility?

import csv, getopt, os, sys, datetime, time
from distutils import version

def usage():
    print
    print "Sets the property in a java property file to the specified value, or adds it if it isn't present."
    print ' -h, --help       Show this help.'
    print ' -f, --file       The property file to read from / edit.'
    print ' -p, --property   The property to set.'
    print ' -v, --value      The value to set the property to.'
    print ' -o, --output     The output file.  If omitted, the change is written back to the input file, if present, the change is written to the output file instead.  If an empty string ("") for -o is given, the result is printed to stdout.'
    print ' -q, --quiet      Does not print any output if there are no errors.'



def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hf:p:v:o:q", ["help", "file=","property=","value=","output=", "quiet"])
    except getopt.GetoptError, err:
        # print help information and exit:
        print
        print str(err) # will print something like "option -a not recognized"
        usage()
        sys.exit(2)

    FILE = ''
    PROPERTY = ''
    VALUE = None
    OUTPUT = None
    QUIET = False

    for o, a in opts:
        if o in ("-h", "--help"):
            usage()
            sys.exit()
        elif o in ("-q", "--quiet"):
            QUIET = True
        elif o in ("-f", "--file"):
            FILE = a
        elif o in ("-p", "--property"):
            PROPERTY = a
        elif o in ("-v", "--value"):
            VALUE = a
        elif o in ("-o", "--output"):
            OUTPUT = a
        else:
            assert False, 'unhandled option'

    if (PROPERTY == ''):
        print
        print 'Please specify a property.'
        usage()
        sys.exit(2)

    if (VALUE == None):
        print
        print 'Please specify a value.'
        usage()
        sys.exit(2)

    if (FILE == ''):
        print
        print 'Please specify a file.'
        usage()
        sys.exit(2)

    if (OUTPUT == None):
        OUTPUT = FILE

    # Load input file
    propertyFile = open(FILE, "rb")
  
    result = ''

    # Scan through the file and do a replace
    propertyFound = False
    line = propertyFile.readline()
    while line <> '':
        if line.strip().startswith( PROPERTY ):
            line = PROPERTY + "=" + VALUE + '\n'
            propertyFound = True

        result += line
        
        line = propertyFile.readline()

    propertyFile.close()

    if not propertyFound:
        result += '\n' + PROPERTY + "=" + VALUE + '\n'

    if (OUTPUT == ''):
        print result
    else:
        outputFile = open(OUTPUT,"w")
        outputFile.write( result )
        outputFile.close()
        if not QUIET:
            print "Property '"+PROPERTY+"' changed to value '"+VALUE+"' in '" + OUTPUT +"'"
            print
        

if __name__ == "__main__":
    main()







