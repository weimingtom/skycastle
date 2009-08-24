#! /usr/bin/python
# Google code issue system to changelist.txt generator.
# Programmed 2008-11 by Hans Haggstrom (zzorn at iki.fi)
# Public domain, adapt and use as you want.

import csv, getopt, os, sys, datetime, time
from distutils import version

def usage():
    print
    print 'Google code issue downloader and changelist generator'
    print ' -h, --help       Show this help'
    print ' -p, --project    The google code project id (this is a unix style user name, as used in the project URL).  This is required.'
    print ' -n, --name       The name to use for the project in the changelist.  Defaults to the project name.'
    print ' -o, --output     The name of the output file to save the report to.  If not specified, will print to stdout.'
    print ' -r, --release    The release to create a changelist for.  All releases up to and including this release are included in the report.  Default is 1.0.'
    print ' -t, --temp       The name of the temporary file to load the issue csv to.  Defaults to issue-changes.csv.'
    print '     --prefix     The prefix used by the project for marking which release an issue belongs to.  Defaults to "Release".  (Currently only its length is used)'
    print ' -q, --quiet      Does not print any output if there are no errors.'



def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hp:n:o:r:t:q", ["help", "project=","name=","output=","release=","temp=", "quiet", "prefix="])
    except getopt.GetoptError, err:
        # print help information and exit:
        print
        print str(err) # will print something like "option -a not recognized"
        usage()
        sys.exit(2)

    APP_NAME = ''
    GOOGLE_CODE_PROJECT_ID = ''
    ISSUE_IMPORT_FILE = "issue-changes.csv"
    OUTPUT_FILE = ''
    RELEASE = '1.0' 
    RELESE_PREFIX = 'Release' 
    QUIET = False

    for o, a in opts:
        if o in ("-h", "--help"):
            usage()
            sys.exit()
        elif o in ("-q", "--quiet"):
            QUIET = True
        elif o in ("-p", "--project"):
            GOOGLE_CODE_PROJECT_ID = a
        elif o in ("-n", "--name"):
            APP_NAME = a
        elif o in ("-o", "--output"):
            OUTPUT_FILE = a
        elif o in ("-r", "--release"):
            RELEASE = a
        elif o in ("-t", "--temp"):
            ISSUE_IMPORT_FILE = a
        elif o in ("--prefix"):
            RELESE_PREFIX = a
        else:
            assert False, 'unhandled option'

    if (GOOGLE_CODE_PROJECT_ID == ''):
        print
        print 'Please specify a google project id using the -p option.'
        usage()
        sys.exit(2)

    if (APP_NAME == ''):
        APP_NAME = GOOGLE_CODE_PROJECT_ID

    # Download issues from google code in csv format
    url = 'http://code.google.com/p/'+GOOGLE_CODE_PROJECT_ID+'/issues/csv?can=1&sort=priority'
    wgetOpts = ' -q '
    if not QUIET:
        print 'Downloading google code project issue list from ' + url
        wgetOpts = ''
    os.system(' wget '+wgetOpts+' -O '+ISSUE_IMPORT_FILE+' "'+url+'" ')

    # Load google code issue export file
    reader = csv.reader(open(ISSUE_IMPORT_FILE, "rb"))
  

    # Read in headers from the file
    headers = {}
    i = 0
    for name in reader.next():
        headers[name] = i
        i += 1



    # Handle each issue, build a list of releases
    releases = {}
    openBugs = []
    for row in reader:
        if len(row) > 0:
            issueNumber = row[headers['ID']]
            kind = row[headers['Type']]
            status = row[headers['Status']]
            priority = row[headers['Priority']]
            milestone = row[headers['Milestone']] [ len(RELESE_PREFIX) : ] 
            owner = row[headers['Owner']]
            summary = row[headers['Summary']]

            release = {}
            if releases.has_key(milestone):
                release = releases[milestone]
            else:
                release['name'] = milestone
                release['features'] = []
                release['bugfixes'] = []
                release['enhancements'] = []
                releases[milestone] = release

            entry = '      * Issue ' + issueNumber + ' ('+priority+'):  ' + summary 

            if status == 'Verified' or status == 'Fixed':
                if kind == 'Defect':
                    release['bugfixes'].append( entry ) 
                if kind == 'Enhancement':
                    release['enhancements'].append( entry ) 

            if (status == 'Accepted' or status == 'Started') and kind == 'Defect':
                openBugs.append( entry ) 

    # Output the releases

    def compareVersion( a, b ):
        # Handle missing version info (LooseVersion doesn't)
        if (a == ''):
            return -1
        if (b == ''):
            return 1
    
        av = version.LooseVersion(a )
        bv = version.LooseVersion(b )
        return cmp( bv, av )

    releaseIds = releases.keys()
    releaseIds.sort( compareVersion )

    output = ''
    NL = '\n'

    output += NL
    output += 'Changelist for ' + APP_NAME + NL

    latestRelease = True
    for releaseId in releaseIds:
        if compareVersion( releaseId, RELEASE ) >= 0:

            release = releases[ releaseId ]
    
            output += NL        
            output += NL        
            output += '  ' + APP_NAME + ' v. ' + release['name'] + NL

            ## The features section is planned for more full length descriptions of major features implemented for the release
            ## and could be pulled from the wiki for example.
            ## TODO: Implement.
            #print
            #print '    Features '
            #for f in release['features']:
            #    print f

            output += NL
            output +=  '    Enhancements ' + NL
            for e in release['enhancements']:
                output += e + NL

            output += NL
            output += '    Fixed bugs ' + NL
            for b in release['bugfixes']:
                output += b + NL

            if latestRelease:                
                latestRelease = False
                output += NL
                output += '    Known open bugs ' + NL
                for b in openBugs:
                    output += b + NL

    output += NL
    output += NL
    output += 'This changelist was automatically generated on ' + str( datetime.date.today() ) + '.' + NL

    if (OUTPUT_FILE == ''):
        print output
    else:
        fileObj = open(OUTPUT_FILE,"w")
        fileObj.write( output )
        fileObj.close()
        if not QUIET:
            print 'Changelist generated and saved to ' + OUTPUT_FILE
            print
        
if __name__ == "__main__":
    main()







