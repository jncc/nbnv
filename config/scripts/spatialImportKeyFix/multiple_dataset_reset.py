__author__ = 'Matt Debont'

import argparse
import subprocess
import csv
import logging
import datetime


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Runs the NBN Dataset Reset Tool against a CSV of datasets')
    parser.add_argument('-i', '--input', type=str, required=True, help='Path to the CSV containing the dataset list')
    parser.add_argument('-t', '--tool', type=str, default='./AccessResetTool.jar',
                        help='The path to the configured dataset access reset tool')

    args = parser.parse_args()

    logger = logging.getLogger('ResetDatasetAccessRequests')
    logger.setLevel(logging.DEBUG)

    fh = logging.FileHandler('%s-%s.log' % ('UpdateAccessRequests',
                                            datetime.datetime.today().strftime('%Y%m%d-%H%M%S')))
    fh.setLevel(logging.DEBUG)

    ch = logging.StreamHandler()
    ch.setLevel(logging.INFO)

    formatter = logging.Formatter('%(message)s')
    fh.setFormatter(formatter)
    ch.setFormatter(formatter)

    logger.addHandler(fh)
    logger.addHandler(ch)

    with open(args.input, 'rb') as csv_in:
        reader = csv.reader(csv_in)
        for line in reader:
            logger.info('java -jar %s %s' % (args.tool, line[0]))
            process = subprocess.Popen('java -jar %s %s' % (args.tool, line[0]), stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            for output in process.stdout:
                logger.info(output)
            process.wait()
