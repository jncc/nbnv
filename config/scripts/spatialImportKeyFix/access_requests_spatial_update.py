__author__ = 'Matt Debont'

import csv
import pyodbc
import argparse
import getpass
import json
import logging
import datetime


def update_spatial_filter(cursor, filter_id, json_filter):
    cursor.execute('UPDATE [TaxonObservationFilter] SET filterJSON = ? WHERE id = ?', (json_filter, filter_id,))


def check_spatial_filter(cursor, orig, new, mapping):
    rows = cursor.fetchall()

    for row in rows:
        j = json.loads(row[1].decode('latin-1'))
        feature = j['spatial']['feature']

        if feature in orig.keys():
            if mapping[feature] is not None and mapping[feature] is not '':
                logger.debug('ORIGINAL JSON %d' % row[0])
                logger.debug(row[1])
                logger.info('CHANGED - %d - %s to %s' % (row[0], j['spatial']['feature'], mapping[feature]))
                j['spatial']['feature'] = mapping[feature]

                out_json = json.dumps(j).encode('latin-1')
                update_spatial_filter(cursor, row[0], out_json)
        elif feature in new.keys():
            logger.info('UNCHANGED - %d - %s' % (row[0], j['spatial']['feature']))
        else:
            logger.info('POSSIBLE REVOKE - %d - %s' % (row[0], j['spatial']['feature']))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Updates JSON AccessRequest blobs to updates spatial object '
                                                 'identifiers after an update to a boundary layer (if keys are '
                                                 'changed)')
    parser.add_argument('-o', '--original', type=str, required=True, help='Path to CSV of the original layer in the '
                                                                          'format [identifier, name]')
    parser.add_argument('-n', '--new', type=str, required=True, help='Path to CSV of the new layer in the format '
                                                                     '[identifier, name]')
    parser.add_argument('-m', '--mappings', type=str, required=True, help='Path to CSV of the mapping between the new '
                                                                          'and old layers in the format '
                                                                          '[identifier_old, identifier_new]')
    parser.add_argument('-s', '--spatial', type=str, required=True, help='Dataset key of spatial layer being updated')

    parser.add_argument('-ds', '--host', type=str, required=True, help='URL of the SQL Server database to update')
    parser.add_argument('-d', '--database', type=str, required=True, help='The database to connect to')
    parser.add_argument('-u', '--user', type=str, required=True, help='A username for the database server')
    parser.add_argument('-p', '--password', type=str, help='A password for the database server, will prompt if not '
                                                           'given')

    parser.add_argument('--api', type=str, help='NBN api URL')
    parser.add_argument('--api_usr', type=str, help='NBN Superadmin User')
    parser.add_argument('--api_pwd', type=str, help='NBN Superadmin Password')

    args = parser.parse_args()

    if args.password is None:
        args.password = getpass.getpass('Enter password for %s:' % args.user)

    logger = logging.getLogger('UpdateAccessRequests')
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

    original = {}
    updated = {}
    mappings = {}

    with open(args.original, 'rb') as orig_file:
        reader = csv.reader(orig_file)
        for l in reader:
            original[l[0]] = l[1]

    with open(args.new, 'rb') as new_file:
        reader = csv.reader(new_file)
        for l in reader:
            updated[l[0]] = l[1]

    with open(args.mappings, 'rb') as mapping_file:
        reader = csv.reader(mapping_file)
        for l in reader:
            mappings[l[0]] = l[1]

    conn = pyodbc.connect('DRIVER={SQL SERVER};SERVER=%s;DATABASE=%s;UID=%s;PWD=%s' % (args.host, args.database,
                                                                                       args.user, args.password))
    curs = conn.cursor()

    logger.info('-----------------------------------------------------------------------------------------------------')
    logger.info('Organisation Access Requests')
    logger.info('-----------------------------------------------------------------------------------------------------')

    curs.execute('SELECT tof.* FROM [TaxonObservationFilter] tof '
                 'INNER JOIN [OrganisationAccessRequest] oar ON oar.filterID = tof.id '
                 'WHERE [filterJSON] like \'%%%s%%\' '
                 'AND ([oar].[responseTypeID] = 1 OR [oar].[responseTypeID] = NULL)'
                 % args.spatial)

    check_spatial_filter(curs, original, updated, mappings)
    conn.commit()

    logger.info('-----------------------------------------------------------------------------------------------------')
    logger.info('User Access Requests')
    logger.info('-----------------------------------------------------------------------------------------------------')

    curs.execute('SELECT tof.* FROM [TaxonObservationFilter] tof '
                 'INNER JOIN [UserAccessRequest] uar ON uar.filterID = tof.id '
                 'WHERE [filterJSON] like \'%%%s%%\' '
                 'AND ([uar].[responseTypeID] = 1 OR [uar].[responseTypeID] = NULL)'
                 % args.spatial)

    check_spatial_filter(curs, original, updated, mappings)
    conn.commit()

    conn.close()
